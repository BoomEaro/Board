package ru.boomearo.board.objects.boards.arcade.pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.MyPet.PetState;
import de.Keyle.MyPet.api.util.locale.Translation;
import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.AbstractPage;
import ru.boomearo.board.objects.boards.AbstractPageList;

public class ArcadePetPage extends AbstractPage {

    private static final DecimalFormat df = new DecimalFormat("#.##");
    
    public ArcadePetPage(AbstractPageList pageList) {
        super(pageList);
    }

    @Override
    public int getTimeToChange() {
        return 15;
    }

    @Override
    public boolean isVisible() {
        return MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer()) != null;
    }

    @Override
    public String getTitle() {
        return "§a§lПитомец";
    }

    @Override
    protected List<AbstractHolder> createHolders() {
        List<AbstractHolder> tmpLines = new ArrayList<AbstractHolder>();
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    return "§7" + mp.getPetName() + (mp.getSkilltree() != null ? " §7[§6" + mp.getSkilltree().getDisplayName() + "§7]" : "");
                }
                return " ";
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

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    int level = mp.getExperience().getLevel();
                    return "§7Уровень: §b" + level;
                }
                return " ";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    return "§7Сытость: §6" + df.format(mp.getSaturation()) + "§7%";
                }
                return " ";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    return "§7Здоровье: §c" + df.format(mp.getHealth()) + "§7/§c" + df.format(mp.getMaxHealth());
                }
                return " ";
            }

        });
        tmpLines.add(new AbstractHolder(this) {

            @Override
            public String getText() {

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    return "§7Сущность: §a" + Translation.getString("Name." + mp.getPetType().name(), mp.getOwner().getLanguage());
                }
                return " ";
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

                MyPet mp = MyPetApi.getMyPetManager().getMyPet(getPageList().getPlayerBoard().getPlayer());

                if (mp != null) {
                    return "§7Статус: §6" + getPetStatus(mp.getStatus());
                }
                return " ";
            }

        });
        return tmpLines;
    }

    private static String getPetStatus(PetState state) {
        switch (state) {
            case Here: return "Здесь";
            case Dead: return "Мёртв";
            case Despawned: return "Скрыт";
            case PetState: return state.name();
            default: return "unknown " + state.name();
        }
    }
}
