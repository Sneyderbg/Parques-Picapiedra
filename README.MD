# Parqués Picapiedra

Esta es una recreación del Parqués Picapiedra, variación del Parqués Colombiano proveniente del Parchís Español

### Reglas básicas:

- ¡POR RELLENAR! 

### Reglas específicas:

- El primer turno en salir se escoge por medio de los dados, el jugador que saque el mayor número inicia el juego. 
- Se tienen 3 oportunidades para sacar las fichas de la cárcel al iniciar el juego, tiene que sacar fichas pares, es decir que  ambos dados tengan el mismo valor. Se sacan todas las fichas. 
- En este caso se puede avanzar solo entre las casillas especiales. Por ende, sacando solo 5, 7, 10, o 12. 
- Si una ficha se encuentra en la casilla llamada SEGURO, esta será una casilla en la que un oponente puede enviar tu ficha a la cárcel, es decir capturar una ficha. 
- El único seguro en la casilla de SALIDA de cualquiera de los jugadores, sin embargo, si alguno de los oponentes tiene la ficha en la cárcel y sale de ella en ese momento puede enviar a la cárcel las fichas que están en esa casilla. 
- Si se sacan pares 3 tres veces se puede sacar una ficha del juego.
- Cuando se encuentre en la casilla de seguro del color de su ficha solo puede ganar si saca 8 en los dados. 
- Si un jugador no envía a la cárcel la ficha de su oponente aún pudiendo hacerlo, otro jugador puede soplar la jugada y enviarle una ficha a la cárcel. 

----------

## Requisitos para compilar este programa:

- OpenJDK-11.0.5.10 o más reciente.
- JavaFX-18.0.1 o más reciente.
- Gradle 8.0

## Para ejecutar el programa, escriba y ejecute:
- En powershell:
```console
.\gradlew run
```
- En bash:
```console
gradlew run
```

## Para compilar y ejecutar un jar sin incluir librerías ni máquinas virtuales de java:
- En powershell:
```console
.\gradlew build jar
```
- En bash:
```console
gradlew build jar
```
Si quiere ejecutar el jar creado, debe especificar la ruta donde se encuentran los módulos del SDK de JavaFX, y los dos módulos principales así: 
```
java --module-path=<path-to>\javafx-sdk-18.0.1\lib --add-modules=javafx.controls,javafx.fxml -jar <path-to>\app.jar
```
El jar que se crea se encuentra de forma predeterminada en: `<project-path>/app/build/libs`

## Para crear una distribución con todas las librerías y JRE incluido:
- En powershell:
```console
.\gradlew build jlink
```
- En bash:
```console
gradlew build jlink
```

### Si solo quiere incluir las librerías puede ejecutar el siguiente comando:
- En powershell:
```console
.\gradlew build jlinkZip
```
- En bash:
```console
gradlew build jlinkZip
```
