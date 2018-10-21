package BotInterfaces;

import questions.QuestionHelper;

import java.io.IOException;
import java.util.Arrays;

public abstract class Dialog implements IOInterface {
    protected QuestionHelper questionHelper;

    protected enum commands {start("/start"), end("/end"), help("/help"), questionHelp("/questionHelp"), next("/next");
        private String command;
        commands(String s) {
            this.command = s;
        }
        public String getCommand(){return command;}
    }

    public void mainDialog() throws IOException {
        var questionShowed = false;
        while (true){
            var userAnswer = read();
            commands parsedCommand = null;

            for (var currentCommand : commands.values()) {
                if (currentCommand.command.equals(userAnswer))
                    parsedCommand = currentCommand;
            }

            if (parsedCommand == null) {
                if (!questionShowed) {
                    write(String.format("Неизвестная команда. Введите %s для помоци или %s для следующего вопроса", commands.help.getCommand(), commands.next.getCommand()));
                    continue;
                }
                if (questionHelper.checkAnswer(userAnswer)) {
                    write(String.format("Ответ верный!\nДля продолжения введите %s, или введите %s для завершения игры", commands.next.getCommand(), commands.end.getCommand()));
                    questionShowed = false;
                } else
                    write(String.format("Неверный ответ!\nДля подсказки по вопросу введите: %s либо перейти к следующему вопросу: %s", commands.help.getCommand(), commands.next.getCommand()));
                continue;
            }
            switch (parsedCommand) {
                case start:
                    write(String.format("%s \n\r", questionHelper.getNextQuestion()));
                    questionShowed = true;
                    break;
                case end:
                    return;
                case help:
                    write(showHelp());
                    break;
                case questionHelp:
                    write(String.format("%s \n\r", questionHelper.getHelp()));
                    break;
                case next:
                    write(String.format("%s \n\r", questionHelper.getNextQuestion()));
                    questionShowed = true;
                    break;
            }
        }
    }

    protected String showHelp(){
        var botInfo = "Вы можете сыграть с ботом в игру: \"Что? Где? Когда?\"";
        var botCommandsInfo = String.format("Чтоб начать игру введите \"%s\", для завершения игры введите: \"%s\"," +
                " Для получения справки введите: \"%s\"", commands.start.getCommand(), commands.end.getCommand(), commands.help.getCommand());
        return String.format("Об игре:\n\r %s\n\r Команды: \n\r%s", botInfo, botCommandsInfo);
    }
}