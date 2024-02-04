package com.example.backendusermanagement.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class AvatarGenerator {
    public static String generateCover(int type, String text, int id) {
        String fontPath = "msyh.ttc";  // 请根据你的系统中的路径进行替换
        String outputPath = String.format("./random_%d.png", id);

        int font_size;
        int image_width, image_height;
        BufferedImage background;

        if (type == 1) {
            font_size = 80;  // 调整字体大小
            String backgroundPath = "background.png";  // 请替换为你的背景图片路径
            image_width = 1132;
            image_height = 576;
            background = readImage(backgroundPath);
            if (text.codePointCount(0, text.length()) > 1) {
                text = String.join(" ", text.split(""));
            }
        } else if (type == 3) {
            font_size = 250;
            image_width = 960;
            image_height = 960;
            background = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = background.createGraphics();
            graphics.setColor(new Color(62, 109, 186, 255));
            graphics.fillRect(0, 0, image_width, image_height);
        } else {
            font_size = 750; // 调整字体大小
            image_width = 960;
            image_height = 960;
            background = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = background.createGraphics();
            graphics.setColor(new Color(62, 109, 186, 255));
            graphics.fillRect(0, 0, image_width, image_height);
            text = text.substring(0, 1);
        }

        Font font = new Font("Arial", Font.PLAIN, font_size);
        Graphics2D graphics = background.createGraphics();
        graphics.setFont(font);

        if (type == 1) {
            String[] lines = {text};
            int y = 330;
            int text_color = 0x2a48a2;  // 蓝色
            for (String line : lines) {
                int x_start = (image_width - graphics.getFontMetrics().stringWidth(line)) / 2;
                graphics.setColor(new Color(text_color));
                graphics.drawString(line, x_start, y);
                y += graphics.getFontMetrics().getHeight();
            }
        } else if (type == 3) {
            String[] letters = text.split("、");
            int text_color = 0xFFFFFF;  // 白色
            int length = letters.length;
            int circle = length < 4 ? 1 : (length < 7 ? 2 : 3);
            int[] y = (length == 4) ? new int[]{160, 480} : ((length == 1) ? new int[]{320} : new int[]{160, 480});
            int[] x = (length == 1) ? new int[]{320} : new int[]{160, 480};

            for (int i = 0; i < circle; i++) {
                int ll = Math.min(3, length - 3 * i);
                for (int j = 0; j < ll; j++) {
                    graphics.setColor(new Color(text_color));
                    graphics.drawString(letters[3 * i + j], x[j], y[i]);
                }
            }
        } else {
            int y_start = -2;
            int text_color = 0xFFFFFF;  // 白色
            int x_start = (image_width - graphics.getFontMetrics().stringWidth(text)) / 2;
            graphics.setColor(new Color(text_color));
            graphics.drawString(text, x_start, y_start);
        }

        saveImage(background, outputPath);
        background.flush();
        background = null;

        String url = uploadCoverMethod(id, getCoverType(type));
        new File(outputPath).delete();
        System.out.println("generate_method: " + url);
        return url;
    }

    private static BufferedImage readImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveImage(BufferedImage image, String outputPath) {
        try {
            ImageIO.write(image, "png", new File(outputPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String uploadCoverMethod(int id, String url) {
        // 实现上传封面的逻辑
        return url;
    }

    private static String getCoverType(int type) {
        switch (type) {
            case 1:
                return "project_cover";
            case 3:
                return "group_cover";
            default:
                return "team_cover";
        }
    }
}
