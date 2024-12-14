Вот улучшенная версия текста, которая выглядит более профессионально и понятно:  

---

# Java Exam  

## Создание базы данных и таблицы для проекта "Arcade Machine"

Для начала создайте базу данных с именем **`arcade_machine`**:

```sql  
CREATE SCHEMA `arcade_machine`;  
```  

Затем в этой базе данных создайте таблицу **`user`** с полями:  

```sql  
CREATE TABLE `arcade_machine`.`user` (  
  `username` VARCHAR(16) NOT NULL,  
  `email` VARCHAR(255) NULL, 
  `password` VARCHAR(32) NOT NULL, 
  `Score_1` INT ZEROFILL NULL,    
  `Score_2` INT ZEROFILL NULL, 
  `Score_3` INT ZEROFILL NULL,     
  `Score_4` INT ZEROFILL NULL     
);  
```  

Эти SQL-запросы помогут вам подготовить структуру базы данных для проекта.


## Source Tetrs: https://github.com/Gaspared/tetris