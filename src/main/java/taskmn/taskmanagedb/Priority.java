package taskmn.taskmanagedb;

public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int id;

    Priority(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
