package EventsPackage;

import java.util.Random;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    private Node head;
    private Node before;
    private Node current;
    private Node tail;
    private TicTacToe tictactoe;

    public HelloEvent(TicTacToe tictactoe) {
        this.head = null;
        this.before = null;
        this.current = null;
        this.tail = null;
        this.tictactoe = tictactoe;
    }

    // whenever a message is sent
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSent = event.getMessage().getContentRaw().toLowerCase();
        String[] message = messageSent.split(" ");
        TextChannel channel = event.getChannel();
        Guild guild = event.getGuild();

        if (event.getAuthor().isBot()) {
            return;
        }
        if (messageSent.equalsIgnoreCase("Hi Larry")) {
            Boolean random = new Random().nextBoolean();
            channel.sendMessage(random ? "Hey babe" : "no").queue();
        } else if (messageSent.equalsIgnoreCase("Goodnight Larry")) {
            channel.sendMessage("Goodnight, see you soon!").queue();
        } else if (messageSent.equalsIgnoreCase("Larry, poo")) {
            channel.sendMessage("poop").queue();
        }
        if (event.getMember().getUser().getAsTag().equals("GoodGuyJason#7020")) {
            if (messageSent.contains(":joy:") || messageSent.contains(":rofl:")) {
                channel.sendMessage(":neutral_face:").queue();
            } else {
                boo(channel);
            }
        }
        //game commands
        if (messageSent.contains("larry, play tictactoe")) {
            boolean start = false;
            try {
                Member player = event.getMessage().getMentionedMembers().get(0);
                channel.sendMessage(event.getMember().getUser().getAsMention() + " is X. " + player.getUser().getAsMention() + " is O.").queue();
                tictactoe.players(event.getMember(), player);
                start = true;
            } catch (Exception e) {
                // when they @ people in the middle of the game the game stops
                channel.sendMessage("That is not a valid user. Start the game again.").queue();
            }
            if (start) {
                tictactoe.display(channel);
                channel.sendMessage("Enter a number from 1-9.").queue();
            }
        } else if (message[0].equals("larry,") && message[1].equals("play")) {
            channel.sendMessage("Larry, let's play connect 4 with @USER").queue();
        }
        //queue
        // fix array out of bounds bug
        if (message[0].equals("larry,")) {
            int move;
            // game commands are numbers
            try {
                move = Integer.parseInt(message[1]);
            } catch (NumberFormatException e) {
                move = 0;
            }

            if (tictactoe.getStart() && move > 0 && move < 10) {
                //valid move
                tictactoe.replace(event.getMember(), move - 1);
            } else {
                //queue commands
                switch (message[1]) {
                    case "queue":
                    case "q":
                        enqueue(new Node(event.getMessage().getMentionedMembers().get(0)));
                        channel.sendMessage(message[2] + " was added to the queue").queue();
                        break;
                    case "remove":
                    case "kill":
                        search(event.getMessage().getMentionedMembers().get(0).getUser());
                        remove(channel, event.getMessage().getMentionedMembers().get(0).getUser());
                        break;
                    case "next":
                        if (head != null) {
                            moveUser(event, head.getMember(), event.getMember().getVoiceState().getChannel());
                            dequeue(event);
                        } else {
                            channel.sendMessage("The queue is empty.").queue();
                        }

                        // move user to voice/text channel
                        break;
                    case "peek":
                        // needs to change text name to a mention
                        if (peek() == null) {
                            channel.sendMessage("The queue is empty.").queue();
                        } else {
                            channel.sendMessage(peek().getUser().getAsMention() + " is next.").queue();
                        }
                        break;
                    case "display":
                        display(event);
                        break;
                    // add a queue option where you can choose where to queue  
                    default:
                        event.getChannel().sendMessage("Sorry, I can't understand you.").queue();
                        break;
                }
            }
        }
    }

    public void boo(TextChannel channel) {
        Random random = new Random();
        String message = null;
        switch (random.nextInt(5)) {
            case 0:
                message = "ew it's Jason";
                break;
            case 1:
                message = ":poop:";
                break;
            case 2:
                message = "shut up";
                break;
            case 3:
                message = "lalalalalalalalalala";
                break;
            case 4:
                message = "men :face_vomiting::face_vomiting:";
                break;
            default:
                message = null;
                break;
        }
        if (message != null) {
            channel.sendMessage(message).queue();
        } 
    }

    public void search(User user) {
        current = head;
        before = null;

        while (current != null && !user.equals(current.getUser())) {
            before = current;
            current = current.getNext();
        }
    }

    public boolean moveUser(GuildMessageReceivedEvent event, Member member, VoiceChannel voiceChannel) {
        if (voiceChannel == null) {
            event.getChannel().sendMessage("You are not connected to a voice channel!").queue();
            return false;
        } else if (member.getVoiceState() == null) {
            event.getChannel().sendMessage(member + " is not in a voice channel.").queue();
            return false;
        } else {
            System.out.println(voiceChannel);
            System.out.println(member.getUser().getName());
            event.getGuild().moveVoiceMember(member, voiceChannel).queue();
            return true;
        }
    }

    public void enqueue(Node node) {

        // head --> tail
        //head and tail are the same thing if queue is empty
        if (head == null) {
            tail = node;
            head = tail;
        } else {
            tail.setNext(node);
            tail = node;
        }
    }

    public void remove(TextChannel channel, User user) {

        // case list is empty or value not in list
        if (current == null || !user.equals(current.getUser())) {
            channel.sendMessage(user.getAsMention() + " is not in the queue.").queue();
            return;
            // case first in list
        } else if (before == null) {
            head = current.getNext();
            // case in the middle of list
        } else {
            before.setNext(current.getNext());
        }
        channel.sendMessage(user.getAsMention() + " was removed.").queue();
    }

    public Node peek() {
        if (isEmpty()) {
            return null;
        } else {
            return head;
        }
    }

    public void dequeue(GuildMessageReceivedEvent event) {
        if (head != null) {
            head = head.getNext();
            return;
        }
        if (head == null) {
            tail = null;
            event.getChannel().sendMessage("The queue is empty.").queue();
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void display(GuildMessageReceivedEvent event) {
        Node current = head;
        int count = 0;

        if (head == null) {
            event.getChannel().sendMessage("The queue is empty.").queue();
            return;
        }

        while (current != null) {
            count++;
            event.getChannel().sendMessage(count + ". " + current.getName()).queue();
            current = current.getNext();
        }
    }
}
