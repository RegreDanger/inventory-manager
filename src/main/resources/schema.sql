CREATE TABLE IF NOT EXISTS category (id TEXT NOT NULL,name TEXT NOT NULL,description TEXT NOT NULL,CONSTRAINT pk_category PRIMARY KEY (id),CONSTRAINT uq_category_name UNIQUE (name));
