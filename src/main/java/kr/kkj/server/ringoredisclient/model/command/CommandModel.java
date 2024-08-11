package kr.kkj.server.ringoredisclient.model.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


//redis cli command model
@Getter
@NoArgsConstructor
public class CommandModel {

    private String name; // 1 명령어 명
    private long arity; // 2 명령어 파라미터의 최소 개수
    private List<String> flags; //3
    private long firstKey; // 4
    private long lastKey; //5
    private long step; //6
    private List<String> aclCategories;// 7
    private List<String> tips; //8
    private List<String> keySpecifications; //9
    private List<CommandModel> subcommands; //10

    public CommandModel(Object cmdObject) {

        try {
            List<Object> cmdInfos = (List<Object>) cmdObject;
            if(!cmdInfos.isEmpty()){
                this.name = (String) cmdInfos.getFirst();
            }
            if(cmdInfos.size() > 1){
                this.arity = (long) cmdInfos.get(1);
            }
            if(cmdInfos.size() > 2){
                this.flags = (List<String>) cmdInfos.get(2);
            }
            if(cmdInfos.size() > 3){
                this.firstKey = (long) cmdInfos.get(3);
            }
            if(cmdInfos.size() > 4){
                this.lastKey = (long) cmdInfos.get(4);
            }
            if(cmdInfos.size() > 5){
                this.step = (long) cmdInfos.get(5);
            }
            if(cmdInfos.size() > 6){
                this.aclCategories = (List<String>) cmdInfos.get(6);
            }
            if(cmdInfos.size() > 7){
                this.tips = (List<String>) cmdInfos.get(7);
            }
            if(cmdInfos.size() > 8){
                this.keySpecifications = (List<String>) cmdInfos.get(8);
            }
            if(cmdInfos.size() > 9){
                if(cmdInfos.get(9) == null || ((List) cmdInfos.get(9)).isEmpty()){
                    return;
                }

                this.subcommands = new ArrayList<>();
                for( var subCmd : (List) cmdInfos.get(9)){
                    if(subCmd == null){
                        continue;
                    }
                    subcommands.add(new CommandModel(subCmd));
                }
            }
        }catch (Exception e) {
            System.out.println(cmdObject);
            e.printStackTrace();
        }



    }

    public boolean emptyCommand() {
        return !StringUtils.hasText(this.name);
    }

    public boolean invalidCommand(String[] commands) {
        if( commands == null || commands.length == 0){
            return true;
        }

        if(this.negativeArity() && commands.length < Math.abs(this.getArity())){
            return true;
        } else if(this.positiveArity() && commands.length != this.getArity()) {
            return true;
        }

        return false;
    }

    private boolean negativeArity(){
        return this.arity < 0;
    }
    private boolean positiveArity(){
        return this.arity >= 0;
    }

    public String findFirstKey(String[] commands) {
        return commands[(int)this.firstKey];
    }
}
