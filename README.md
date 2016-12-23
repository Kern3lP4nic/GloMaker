# GloMaker

Con questo strumento è possibile automatizzare il processo di aggiornamento del glossario, inerente alla documentazione del progetto del corso di SWE. 

Per il suo corretto funzionamento è necessario aver installato nella macchina [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

Lo script viene eseguito da console, e deve essere posizionato all'interno della cartella insieme al file contenente tutti termini del glossario e alla directory contenente i documenti da esaminare. Una volta compilato `javac Main.java` ed eseguito `java Main`, il programma chiederà all'utente di selezionare il file principale del glossario contenente **SOLAMENTE** i termini con le loro definizioni, e successivamente la directory dove cercare ricorsivamente nuovi termini da aggiungere al glossario. 

I termini nel glossario devono essere definiti in questo modo
```
\newglossaryentry{wordName}{
    name=wordName,
    description={wordDefinition}
}
```
mentre i termini che vengono cercati ricorsivamente in altri file devono essere definiti in questo modo
```
\gl{wordName}
```

Lo script riscriverà interamente il glossario ad ogni sua esecuzione, quindi assicurarsi di eseguire una copia di backup per evitare danni o perdite.

## ChangeLog
***v0.0.1***
- Versione primitiva dello script.

## Bug List
- Lo script non funziona correttamente se nella descrizione del termine è presente la parentesi grafa }

## License
```
The MIT License

Copyright (c) 2016 Daniele Favaro

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
