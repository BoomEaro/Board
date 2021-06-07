package ru.boomearo.board.objects.boards.test.pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class TestTechPage extends AbstractPage {

    private static final DecimalFormat dfv = new DecimalFormat("#.#");
    
    public TestTechPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 60;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getTitle() {
        return "§c§lТестовый сервер";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Тех. информация:";
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Тиков в сек:";
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§f" + getTpsFormat(Board.getInstance().getTpsRunnable().getTps());
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 100;
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                double cpuUsage = -1;
                try {
                    com.sun.management.OperatingSystemMXBean systemBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                    cpuUsage = systemBean.getProcessCpuLoad() * 100;
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
                String usage = getCpuFormat(cpuUsage);
                if (cpuUsage < 0) {
                    usage = "Не доступена";
                }
                return "&6Нагрузка: &c" + usage;
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 550;
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                double cpuUsage = -1;
                try {
                    com.sun.management.OperatingSystemMXBean systemBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                    cpuUsage = systemBean.getSystemCpuLoad() * 100;
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
                String usage = getCpuFormat(cpuUsage);
                if (cpuUsage < 0) {
                    usage = "Не доступен";
                }
                return "&6Процессор: &c" + usage;
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 550;
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
                return "§6Исп. памяти:";
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Занято: §c" + dfv.format((double) ((Runtime.getRuntime().totalMemory() / 1024d / 1024d) - (Runtime.getRuntime().freeMemory() / 1024d / 1024d))) + " мб";
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 200;
            }
            
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Свободно: §c" + dfv.format((double) (Runtime.getRuntime().freeMemory() / 1024d / 1024d)) + " мб";
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 200;
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Выделено: §c" + dfv.format((double) (Runtime.getRuntime().totalMemory() / 1024d / 1024d)) + " мб";
            }
            
            @Override 
            public long getMaxCacheTime() {
                return 200;
            }
        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {
                return "§6Максимум: §c" + dfv.format((double) (Runtime.getRuntime().maxMemory() / 1024d / 1024d)) + " мб";
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

            @Override
            public long getMaxCacheTime() {
                return 200;
            }
        });
        tmpLines.add(new AbstractHolder(this) {
            
            @Override
            public String getText() {
                return "§6Чанков: §c" + Board.getInstance().getTpsRunnable().getChunks();
            }

            @Override
            public long getMaxCacheTime() {
                return 200;
            }
        });
        return tmpLines;
    }

    private static String getTpsFormat(double tps) {
        if (tps > 20) {
            tps = 20;
        }
        if (tps <= 15) {
            return "§4" + dfv.format(tps);
        }
        if (tps <= 16) {
            return "§c" + dfv.format(tps);
        }
        if (tps <= 17) {
            return "§e" + dfv.format(tps);
        }
        if (tps <= 18) {
            return "§6" + dfv.format(tps);
        }
        if (tps <= 19) {
            return "§2" + dfv.format(tps);
        }
        if (tps <= 20) {
            return "§a" + dfv.format(tps);
        }
        return "§a*" + dfv.format(tps);
    }
    
    private static String getCpuFormat(double cpu) {
        if (cpu <= 15) {
            return "§7" + dfv.format(cpu) + "%";
        }
        if (cpu <= 45) {
            return "§f" + dfv.format(cpu) + "%";
        }
        if (cpu <= 75) {
            return "§c" + dfv.format(cpu) + "%";
        }
        if (cpu <= 100) {
            return "§4" + dfv.format(cpu) + "%";
        }
        return "§7" + dfv.format(cpu) + "%";
    }
    
}
