package EventsPackage;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TicTacToe {

    private String[] grid = new String[]{":regional_indicator_t:", ":regional_indicator_i:", ":regional_indicator_c:", ":regional_indicator_t:", ":regional_indicator_a:", ":regional_indicator_c:", ":regional_indicator_t:", ":regional_indicator_o:", ":regional_indicator_e:"};
    private TextChannel channel;
    private Member player1;
    private Member player2;
    private Member winner = null;
    private boolean start = false;

    public boolean getStart() {
        return start;
    }

    public void players(Member player1, Member player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void display(TextChannel channel) {
        this.channel = channel;
        for (int i = 0; i <= 6; i += 3) {
            channel.sendMessage(grid[i] + " " + grid[i + 1] + " " + grid[i + 2]).queue();
        }
        start = true;
    }

    public void replace(Member player, int selection) {
        String temp;
        if (player.equals(player1) || player.equals(player2)) {
            temp = player.equals(player1) ? ":x:" : ":o:";
        } else {
            return;
        }
        if (!grid[selection].equals(":x:") && !grid[selection].equals(":o:")) {
            grid[selection] = temp;
        } else {
            channel.sendMessage("That position was already taken or does not exist.").queue();
        }
        display(channel);
        if (checkWinner()) {
            channel.sendMessage(winner.getUser().getAsMention() + " won the game!").queue();
            reset();
            return;
        } else {
            for (int i = 0; i < 9; i++) {
                if (!grid[i].equals(":x:") && !grid[i].equals(":o:")) {
                    return;
                }
            }
        }
        //tie check
        channel.sendMessage("Looks like theres a tie. Try harder next time.").queue();
    }

    public boolean checkWinner() {
        // check rows
        for (int i = 0; i <= 6; i += 3) {
            if (grid[i].equals(grid[i + 1]) && grid[i].equals(grid[i + 2])) {
                winner = grid[i].equals(":x:") ? player1 : player2;
                return true;
            }
        }
        // check columns
        for (int i = 0; i <= 2; i++) {
            if (grid[i].equals(grid[i + 3]) && grid[i].equals(grid[i + 6])) {
                if (grid[i].equals(":regional_indicator_t:")) {
                    continue;
                }
                winner = grid[i].equals(":x:") ? player1 : player2;
                return true;
            }
        }
        if (grid[0].equals(grid[4]) && grid[0].equals(grid[8])) {
            winner = grid[0].equals(":x:") ? player1 : player2;
            return true;
        } else if (grid[2].equals(grid[4]) && grid[2].equals(grid[6])) {
            winner = grid[2].equals(":x:") ? player1 : player2;
            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        winner = null;
        grid = new String[]{":regional_indicator_t:", ":regional_indicator_i:", ":regional_indicator_c:", ":regional_indicator_t:", ":regional_indicator_a:", ":regional_indicator_c:", ":regional_indicator_t:", ":regional_indicator_o:", ":regional_indicator_e:"};
        start = false;
    }

}
