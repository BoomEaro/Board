package ru.boomearo.board.objects.boards;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import ru.boomearo.board.Board;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.utils.DateUtil;

public class TestBoard extends AbstractBoard {

	public TestBoard() {
        super("TestBoard", 1);
    }

	@Override
	public List<AbstractPage> getPages(PlayerBoard player) {
		
		List<AbstractPage> tmpBoards = new ArrayList<AbstractPage>();
		tmpBoards.add(new AbstractPage(player) {
			
			@Override
			public int getTimeToChange() {
				return 10;
			}

			@Override
			public String getTitle() {
				return "§c§lТестовый сервер";
			}
			
			@Override
			public List<AbstractHolder> createHolders() {
				List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return "§cТекущие координаты:";
					}
					
				});
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return "§fX: §c" + df.format(this.getPage().getPlayerBoard().getPlayer().getLocation().getX());
					}
					
					@Override 
					public long getMaxCacheTime() {
						return 100;
					}
				});
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return "§fY: §c" + df.format(this.getPage().getPlayerBoard().getPlayer().getLocation().getY());
					}
					
					@Override 
					public long getMaxCacheTime() {
						return 100;
					}
				});
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return "§fZ: §c" + df.format(this.getPage().getPlayerBoard().getPlayer().getLocation().getZ());
					}
					
					@Override 
					public long getMaxCacheTime() {
						return 100;
					}
				});
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						Location loc = this.getPage().getPlayerBoard().getPlayer().getLocation();
						return "§fМир: §c" + loc.getWorld().getName();
					}
					
					@Override 
					public long getMaxCacheTime() {
						return 100;
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
						return "§6Аптайм:";
					}
					
				});
				tmpLines.add(new AbstractHolder(this) {

					@Override
					public String getText() {
						return "§c" + DateUtil.formatedTimeSimple(System.currentTimeMillis() - Board.uptime, true);
					}
					
					@Override 
					public long getMaxCacheTime() {
						return 100;
					}
				});
				return tmpLines;
			}

			@Override
			public boolean isVisible() {
				return true;
			}

		});
		tmpBoards.add(new AbstractPage(player) {

			@Override
			public int getTimeToChange() {
				return 60;
			}
			
			@Override
			public String getTitle() {
				return "§c§lТестовый сервер";
			}

			@Override
			public List<AbstractHolder> createHolders() {
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

			@Override
			public boolean isVisible() {
				return true;
			}
		});
		return tmpBoards;
	}
	
	private String getTpsFormat(double tps) {
		if (tps > 20) {
			tps = 20;
		}
		if (tps <= 15) {
			return "§4" + df.format(tps);
		}
		if (tps <= 16) {
			return "§c" + df.format(tps);
		}
		if (tps <= 17) {
			return "§e" + df.format(tps);
		}
		if (tps <= 18) {
			return "§6" + df.format(tps);
		}
		if (tps <= 19) {
			return "§2" + df.format(tps);
		}
		if (tps <= 20) {
			return "§a" + df.format(tps);
		}
		return "§a*" + df.format(tps);
	}
	
	private String getCpuFormat(double cpu) {
		if (cpu <= 15) {
			return "§7" + df.format(cpu) + "%";
		}
		if (cpu <= 45) {
			return "§f" + df.format(cpu) + "%";
		}
		if (cpu <= 75) {
			return "§c" + df.format(cpu) + "%";
		}
		if (cpu <= 100) {
			return "§4" + df.format(cpu) + "%";
		}
		return "§7" + df.format(cpu) + "%";
	}

	private static final DecimalFormat df = new DecimalFormat("#.##");
	private static final DecimalFormat dfv = new DecimalFormat("#.#");
}
