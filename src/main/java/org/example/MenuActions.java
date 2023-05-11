package org.example;

public enum MenuActions {
    ADD_PLACE(1),
    ADD_BOOK(2),
    EXIT(3),
    SHOW_ALL_PLACES(4),
    SHOW_ALL_BOOKS(5),
    UPDATE_PLACE(6),
    UPDATE_BOOK(7),
    DELETE_PLACE(8),
    DELETE_BOOK(9),
    SHOW_1_FIELD_IN_ALL_BOOKS(10),
    SHOW_2_FIELD_IN_ALL_BOOKS_IN_LEXIC_ORDER(11),
    DEFAULT_DATA_FROM_FILE(12),
    SHOW_AUTHORS_IN_WARDROBE(13),
    SHOW_SUMM_WEIGHT_IN_WARDROBE(14);

    private final int action;

    MenuActions(int action){
        this.action = action;
    }

    public int getAction(){
        return action;
    }


}
