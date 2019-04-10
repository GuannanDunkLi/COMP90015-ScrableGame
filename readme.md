COMP90015-ScableGame
COMP90015-Assignment2-ScrableGame

***

This distributed game used multithread and socket programming, and it achieved good performance in concurrency and robustness

**to run this project**

two commond lines:
1. java -jar GameServer.jar 1234 (1234 is the port number)
2. java -jar Client.jar localhost 1234 (localhost could be specified to any IP address, and 1234 is the port number)

hint: you should run server first, and you can run several clients to play this game

**sequence diagram**

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/overview.png)

**functions**

[1. Login](#Login)

[2. Invitation Hall](#InvitationHall)

[3. Scrabble Game Process](#ScrabbleGameProcess)

[4. Exception Handling](#ExceptionHandling)

***

## Login

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/login1.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/login2.png)

after you logs in, the server will record the logging information:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/1.png)

## InvitationHall

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/inv1.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/inv2.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/inv3.png)

let's have three players, and two of them play this game:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/2.png)

## ScrabbleGameProcess

the correctness of word is determined by all players:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/4.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/5.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/6.png)

if we choose agree:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/7.png)

then another player want to watch this game:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/8.png)

the watcher cannot play this game:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/9.png)

after one player logs out, the game is over, and will declare the winner:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/10.png)

all game information is stored in server's log system:

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/11.png)

## ExceptionHandling

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/exc1.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/ex2.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/exc3.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/exc4.png)

![img](https://github.com/GuannanDunkLi/COMP90015-ScrableGame/blob/master/images/exc5.png)

