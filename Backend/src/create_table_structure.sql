create table Ingredient
(
    id   int auto_increment
        primary key,
    name varchar(64) not null,
    constraint Ingredient_name_uindex
        unique (name)
);

create table User
(
    id         int auto_increment
        primary key,
    name       varchar(32)                          not null,
    password   varchar(512)                         not null,
    created_at datetime   default CURRENT_TIMESTAMP not null,
    is_admin   tinyint(1) default 0                 not null,
    constraint User_name_uindex
        unique (name)
);

create table Recipe
(
    id               int auto_increment
        primary key,
    name             varchar(32)                        not null,
    preptime_minutes int                                not null,
    difficulty       int                                null,
    instruction      text                               null,
    created_at       datetime default CURRENT_TIMESTAMP not null,
    creator_id       int                                not null,
    constraint RecipeCreator___fk
        foreign key (creator_id) references User (id)
);

create table Favourite
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    recipe_id int not null,
    constraint FavouriteRecipe___fk
        foreign key (recipe_id) references Recipe (id),
    constraint FavouriteUser___fk
        foreign key (user_id) references User (id)
);

create table Rating
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    recipe_id int not null,
    value     int not null,
    constraint RatingRecipe___fk
        foreign key (recipe_id) references Recipe (id),
    constraint RatingUser___fk
        foreign key (user_id) references User (id)
);

create table RecipeIngredient
(
    id            int auto_increment
        primary key,
    ingredient_id int  not null,
    recipe_id     int  not null,
    amount        text not null,
    constraint RecipeIngredientIngredient___fk
        foreign key (ingredient_id) references Ingredient (id),
    constraint RecipeIngredientRecipe___fk
        foreign key (recipe_id) references Recipe (id)
);

insert into User (name, password, is_admin) values ('admin', '4aeb2000b9de5858f5e5e0b7eda52f253caf19582c67cbbb453be6987ecc1baf27d75670e39f78058fb1ebee3d16b83d1cbdc8d3628636377b2458ea5bf12ff2', 1)


