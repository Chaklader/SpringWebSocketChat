SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `chat_app` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `chat_app`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `password` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (name))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `chat_app`.`conversations` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `conversation_key` VARCHAR(50) NOT NULL,
  `conversation_starter_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (conversation_key),
  INDEX `conversation_starter_idx` (`conversation_starter_id` ASC),
  CONSTRAINT `conversation_starter_fk`
    FOREIGN KEY (`conversation_starter_id`)
    REFERENCES `chat_app`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `chat_app`.`messages` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `body` VARCHAR(700) NOT NULL,
  `conversation_id` INT(11) NOT NULL,
  `sender_id` INT(11) NOT NULL,
  `date_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `message_conversation_idx` (`conversation_id` ASC),
  INDEX `message_sender_idx` (`sender_id` ASC),
  CONSTRAINT `message_conversation_fk`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `chat_app`.`conversations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `message_sender_fk`
    FOREIGN KEY (`sender_id`)
    REFERENCES `chat_app`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `chat_app`.`participants` (
  `user_id` INT(11) NOT NULL,
  `conversation_id` INT(11) NOT NULL,
  UNIQUE KEY (user_id, conversation_id),
  INDEX `participants_user_idx` (`user_id` ASC),
  INDEX `participants_conversation_idx` (`conversation_id` ASC),
  CONSTRAINT `participants_user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `chat_app`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `participants_conversation_fk`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `chat_app`.`conversations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
