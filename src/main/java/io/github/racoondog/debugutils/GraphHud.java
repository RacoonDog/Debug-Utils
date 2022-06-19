package io.github.racoondog.debugutils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class GraphHud extends DrawableHelper {
    public GraphHud() {}

    public void render(MatrixStack matrixStack) {
        MinecraftClient.getInstance().getProfiler().push("graphs");

        int i = MinecraftClient.getInstance().getWindow().getScaledWidth();

        for (int j = 0; j < DebugUtils.metrics.size(); j++) {
            ModMetricsData metrics = DebugUtils.metrics.get(j);

            int y = (j / 2) * 80;

            int x = 0;
            if (j % 2 == 1) x = i - Math.min(i / 3, 240);

            this.renderGraph(matrixStack, metrics, x, y, i / 3);
        }

        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderGraph(MatrixStack matrixStack, ModMetricsData metricsData, int x, int y, int width) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        RenderSystem.disableDepthTest();

        int startIndex = metricsData.metricsData.getStartIndex();
        int currentIndex = metricsData.metricsData.getCurrentIndex();
        long[] samples = metricsData.metricsData.getSamples();
        int m = Math.max(0, samples.length - width);
        int n = samples.length - m;
        int k = metricsData.metricsData.wrapIndex(startIndex + m);
        long o = 0L;
        int p = Integer.MAX_VALUE;
        int q = Integer.MIN_VALUE;

        for(int i = 0; i < n; ++i) {
            int s = (int)(samples[metricsData.metricsData.wrapIndex(k + i)] / 1000000L);
            p = Math.min(p, s);
            q = Math.max(q, s);
            o += (long)s;
        }

        int height = MinecraftClient.getInstance().getWindow().getScaledHeight() - y;
        fill(matrixStack, x, height - 60, x + n, height, -1873784752);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        int l = x;
        for(Matrix4f matrix4f = AffineTransformation.identity().getMatrix(); k != currentIndex; k = metricsData.metricsData.wrapIndex(k + 1)) {
            int t = metricsData.getLineHeight(samples[k]);
            int u = metricsData.red;
            int v = metricsData.staticColor == 0 ? this.getMetricsLineColor(MathHelper.clamp(t, 0, u), u >> 2, u) : metricsData.staticColor;
            int a = v >> 24 & 255;
            int r = v >> 16 & 255;
            int g = v >> 8 & 255;
            int b = v & 255;
            bufferBuilder.vertex(matrix4f, (float)(l + 1), (float)height, 0.0F).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix4f, (float)(l + 1), (float)(height - t + 1), 0.0F).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix4f, (float)l, (float)(height - t + 1), 0.0F).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix4f, (float)l, (float)height, 0.0F).color(r, g, b, a).next();
            ++l;
        }

        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        if (textRenderer != null) {
            fill(matrixStack, x + 1, height - 60 + 1, x + textRenderer.getWidth(metricsData.text) + 2, height - 60 + 10, -1873784752);
            textRenderer.draw(matrixStack, metricsData.text, (float)(x + 2), (float)(height - 60 + 2), 14737632);
        }

        this.drawHorizontalLine(matrixStack, x, x + n - 1, height - 60, -1);
        this.drawHorizontalLine(matrixStack, x, x + n - 1, height - 1, -1);
        this.drawVerticalLine(matrixStack, x, height - 60, height, -1);
        this.drawVerticalLine(matrixStack, x + n - 1, height - 60, height, -1);

        if (textRenderer != null) {
            String string = "" + p + " " + metricsData.unit + " min";
            String string2 = o / (long)n + " " + metricsData.unit + " avg";
            String string3 = "" + q + " " + metricsData.unit + " max";
            int textHeight = height - 60 - 9;

            textRenderer.drawWithShadow(matrixStack, string, x + 2, textHeight, 14737632);
            textRenderer.drawWithShadow(matrixStack, string2, x + (n - textRenderer.getWidth(string2)) / 2.0f, textHeight, 14737632);
            textRenderer.drawWithShadow(matrixStack, string3, x + n - textRenderer.getWidth(string3), textHeight, 14737632);
        }

        RenderSystem.enableDepthTest();
    }

    private int getMetricsLineColor(int value, int yellowValue, int redValue) {
        return value < yellowValue ? this.interpolateColor(-16711936, -256, (float)value / (float)yellowValue) : this.interpolateColor(-256, -65536, (float)(value - yellowValue) / (float)(redValue - yellowValue));
    }

    private int interpolateColor(int color1, int color2, float dt) {
        int i = color1 >> 24 & 255;
        int j = color1 >> 16 & 255;
        int k = color1 >> 8 & 255;
        int l = color1 & 255;
        int m = color2 >> 24 & 255;
        int n = color2 >> 16 & 255;
        int o = color2 >> 8 & 255;
        int p = color2 & 255;
        int q = MathHelper.clamp((int)MathHelper.lerp(dt, (float)i, (float)m), 0, 255);
        int r = MathHelper.clamp((int)MathHelper.lerp(dt, (float)j, (float)n), 0, 255);
        int s = MathHelper.clamp((int)MathHelper.lerp(dt, (float)k, (float)o), 0, 255);
        int t = MathHelper.clamp((int)MathHelper.lerp(dt, (float)l, (float)p), 0, 255);
        return q << 24 | r << 16 | s << 8 | t;
    }
}
