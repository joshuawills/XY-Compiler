# Config

The 'xy' compiler allows users to specify various compiler options in a 'xy.config' file.<br />
Variables can either be set to true or false.
'#' can be used to comment out lines

| Option                | Description                                                   | Default
| -----------           | -----------                                                   | ----------- |
| FLAG-UNUSED           | Will warn user of unused parameters & functions               | true
| MANDATE-BRACKETS      | Will warn user if expressions not wrapped in () and {}        | false
| SNAKE-CASE            | Mandate variables follow "snake_case" pattern                 | false
| CAMEL-CASE            | Mandate variables follow "camel_case" pattern                 | false