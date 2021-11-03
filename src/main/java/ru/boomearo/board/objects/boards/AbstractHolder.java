package ru.boomearo.board.objects.boards;

import org.bukkit.ChatColor;

import ru.boomearo.board.Board;

public abstract class AbstractHolder {

    //private static final int maxLen = 32;

    //private final int maxCd = 5;

    //private static final char COLOUR_CHAR = '§';
    //private ChatColor colorReset = ChatColor.RESET;

    //private int index = 0;
    //private int cd = 1;
    //private boolean isCd = false;

    //private boolean revert = false;

    //private List<String[]> cache = null;
    private String[] cache = null;
    private long cacheTime = System.currentTimeMillis();

    private final AbstractPage page;

    public AbstractHolder(AbstractPage page) {
        this.page = page;
    }

    public AbstractPage getPage() {
        return this.page;
    }

    public String[] getResult() {

        //Если кеша нет, создаем его делая запрос. Потом возвращаем кеш
        if (this.cache == null) {
            createCacheAnimation(getValidText());
            return this.cache;
        }
        //Если время кеша вышло, обновляем кеш делая запрос. Если время еще не вышло, возвращает кешированное значение
        if ((System.currentTimeMillis() - this.cacheTime) > getMaxCacheTime()) {
            createCacheAnimation(getValidText());
            this.cacheTime = System.currentTimeMillis();
        }

        return this.cache;

    }

    private String getValidText() {
        try {
            String text = getText();
            if (text != null) {
                return text;
            }

            return "null";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    protected abstract String getText();

    //По умолчанию размер кеша = 5 секунд. Хотя допустимо указывать еще милисекунды.
    //Этот метод может переопределить наследник, что бы иметь другое время.
    public long getMaxCacheTime() {
        return 1000 * 5;
    }
	
	/*private String[] getCurrentCache() {
		
		if (this.index > (this.cache.size() - 1)) {
			this.index = 0;
		}
		
		String[] cache = this.cache.get(this.index);
		this.index++;
		return cache;
	}*/

    private void createCacheAnimation(String text) {
        //List<String[]> tmp = new ArrayList<String[]>();

        this.cache = getSplitMsgs(text);
		
		/*if (text.length() <= 32) {
			tmp.add(getSplitMsgs(text));
			this.cache = tmp;
			return;
		}
		
		int maxIter = text.length() - maxLen;
		
		//next
		for (int i = 0; i < maxIter; i++) {
			tmp.add(getSplitMsgs(next(i, text)));
		}
		
		//back
		for (int i = 0; i < maxIter; i++) {
			tmp.add(getSplitMsgs(back(i, text)));
		}
		
		this.cache = tmp;*/
    }
	
	/*private String getCut(String text) {
		
		if (this.tmp == null) {
			this.tmp = text;
			this.cacheTime = System.currentTimeMillis();
		}
		
		if ((System.currentTimeMillis() - this.cacheTime) > getMaxCacheTime()) {
			this.tmp = text;
			this.cacheTime = System.currentTimeMillis();
		}
		
		String msg = this.tmp;
		
		if (msg.length() <= maxLen) {
			return msg;
		}
		
		int maxIter = msg.length() - maxLen;
		
		String cut;
		if (this.revert) {
			
			if (this.isCd) {
				if (this.cd >= maxCd) {
					this.cd = 1;
					this.isCd = false;
				}
				this.cd++;
				return back(msg);
			}
			
			if (this.index >= (maxIter)) {
				this.revert = false;
				this.index = 0;
				
				this.isCd = true;
				
				return next(msg);
			}
			cut = back(msg);
			this.index++;
		}
		else {
			
			if (this.isCd) {
				if (this.cd >= maxCd) {
					this.cd = 1;
					this.isCd = false;
				}
				this.cd++;
				return next(msg);
			}
			
			if (this.index >= (maxIter)) {
				this.revert = true;
				this.index = 0;
				
				this.isCd = true;
				
				return back(msg);
			}
			cut = next(msg);
			
			this.index++;
		}
		
		//getContext().getLogger().info("test " + this.index + " | " + maxLen + " | " + maxIter + " | " + this.revert + " | " + cut);
		
		return cut;
	}*/
	
	/*private String next(int index, String msg) {
		
		return msg.substring(index, (index + maxLen));
	
	}*/
	
	
	/*private String back(int index, String msg) {
		return msg.substring((msg.length() - maxLen) - index, msg.length() - index);
	}*/

    private String[] getSplitMsgs(String text) {
        int maxLenght = Board.maxLength;

        StringBuilder prefix = new StringBuilder(text.substring(0, text.length() >= maxLenght ? maxLenght : text.length()));
        StringBuilder suffix = new StringBuilder(text.length() > maxLenght ? text.substring(maxLenght) : "");

        if (prefix.length() > 1 && prefix.charAt(prefix.length() - 1) == '§') {

            prefix.deleteCharAt(prefix.length() - 1);
            suffix.insert(0, '§');
        }
        if (text.length() > maxLenght) {
            suffix.insert(0, ChatColor.getLastColors(prefix.toString()));
        }

        String suf = suffix.toString();

        //Board.getContext().getLogger().info("testcc '" + prefix.toString() + suf + "' " + suf.length());

        if (suf.length() > maxLenght) {
            suf = suf.substring(0, maxLenght);
            //Board.getContext().getLogger().info("testff2 '" + suf + "' " + suf.length());
        }

        return new String[]{prefix.length() > maxLenght ? prefix.toString().substring(0, maxLenght) : prefix.toString(), suf};

    }
	
	/*private String fixColor(String msg) {
        StringBuilder sb = new StringBuilder(msg);
        if (sb.charAt(sb.length() - 1) == COLOUR_CHAR) {
            sb.setCharAt(sb.length() - 1, ' ');
        }

        if (sb.charAt(0) == COLOUR_CHAR) {
            ChatColor c = ChatColor.getByChar(sb.charAt(1));
            if (c != null) {
            	this.colorReset = c;
                sb = new StringBuilder(msg);
                if (sb.charAt(0) != ' ') {
                    sb.setCharAt(0, ' ');
                }
            }
        }
        
        return this.colorReset + sb.toString();
	}*/


}
