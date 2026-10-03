## Taller: Patrón de diseño Memento (editor de texto con historial)

Editor de texto por terminal que guarda y restaura el estado del documento aplicando el
patrón Memento.

| Paquete       | Clase       | Rol del patrón |
| ------------- | ----------- | -------------- |
| `originator`  | `Editor`    | Originator: dueño del estado, crea y restaura sus Mementos |
| `memento`     | `Memento`   | Memento: copia inmutable del estado (interfaz ancha sólo para el Originator) |
| `memento`     | `SelloOriginador` | Sello que identifica al Originator ante el Memento |
| `caretaker`   | `Historial` | Caretaker: pila LIFO de Mementos, sin acceso al contenido |
| `main`        | `Main`      | Escenario de prueba del taller |
| `main`        | `Consola`   | Menú interactivo de terminal |

### Compilar y ejecutar

```bash
cd Memento
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin main.Main          # menú interactivo
java -cp bin main.Main demo     # escenario de prueba del taller
java -cp bin main.Main --help   # ayuda
```

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
# memento
