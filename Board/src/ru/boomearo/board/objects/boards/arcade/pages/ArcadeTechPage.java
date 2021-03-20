package ru.boomearo.board.objects.boards.arcade.pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class ArcadeTechPage extends AbstractPage {

    private static final DecimalFormat df = new DecimalFormat("#.##");
    
    public ArcadeTechPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 60;
    }


    @Override
    public boolean isVisible() {
        return getPageList().getPlayerBoard().isDebugMode();
    }

    @Override
    public String getTitle() {
        return "§c§lСервер";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Нагрузка на сервер:";
            }


        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                double tps = Board.getInstance().getTpsRunnable().getTps();

                String forTps = getTpsFormat(tps);

                return "§6TPS: " + forTps;

            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return " ";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Использование памяти:";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {

                return getMemoryPrecentColor();
            }

            @Override
            public long getMaxCacheTime() {
                return 200;
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return " ";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Сущностей: §c" + Board.getInstance().getTpsRunnable().getEntites();
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Чанков: §c" + Board.getInstance().getTpsRunnable().getChunks();
            }

        });
        return tmpLines;
    }

    private static String getFormatMemory(String colorFirst, String colorSecond) {
        double maxMemory = (Runtime.getRuntime().totalMemory() / 1024d / 1024d);
        double currentMemory = (maxMemory - (Runtime.getRuntime().freeMemory() / 1024d / 1024d));

        double i1 = maxMemory / 23.0D;
        double proc = currentMemory / i1;
        StringBuffer sb = new StringBuffer("▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎▎");

        sb.insert((int)proc, colorFirst + "▎§0");
        return colorSecond + sb.toString();
    }

    private static String getMemoryPrecentColor() {
        double maxMemory = (Runtime.getRuntime().totalMemory() / 1024d / 1024d);
        double currentMemory = (maxMemory - (Runtime.getRuntime().freeMemory() / 1024d / 1024d));

        double i1 = maxMemory / 100.0D;
        double proc = currentMemory / i1;

        if (proc <= 10) {
            return getFormatMemory("§f", "§f");
        }
        if (proc <= 25) {
            return getFormatMemory("§7", "§f");
        }
        if (proc <= 50) {
            return getFormatMemory("§2", "§a");
        }
        if (proc <= 75) {
            return getFormatMemory("§e", "§6");
        }
        if (proc <= 90) {
            return getFormatMemory("§e", "§6");
        }
        if (proc <= 100) {
            return getFormatMemory("§4", "§4");
        }
        return getFormatMemory("§7", "§f");
    }

    private static String getTpsFormat(double tps) {
        if (tps > 20) {
            tps = 20;
        }
        if (tps <= 5) {
            return "§4" + df.format(tps) + " (ужасная)";
        }
        if (tps <= 10) {
            return "§4" + df.format(tps) + " (гиганская)";
        }
        if (tps <= 15) {
            return "§4" + df.format(tps) + " (большая)";
        }
        if (tps <= 16) {
            return "§c" + df.format(tps) + " (высокая)";
        }
        if (tps <= 17) {
            return "§e" + df.format(tps) + " (средняя)";
        }
        if (tps <= 18) {
            return "§6" + df.format(tps) + " (легкая)";
        }
        if (tps <= 19) {
            return "§2" + df.format(tps) + " (небольшая)";
        }
        if (tps <= 20) {
            return "§a" + df.format(tps) + " (отсутствует)";
        }
        return "§a*" + df.format(tps);
    }
}
