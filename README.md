# Lexi App - [Landing Page](https://thelastcolor.github.io/Lexi-Web/)
[![Android CI](https://github.com/TheLastColor/lexi-app/actions/workflows/android.yml/badge.svg)](https://github.com/TheLastColor/lexi-app/actions/workflows/android.yml)

## Manual técnico
- Descargar el código fuente (rama main)
- Descargar [AndroidStudio](https://developer.android.com/studio) o IDE preferido para desarrollar en Android nativo
- En el tab File -> Open abrir la carpeta donde se descargó el codigo fuente
- Crear cuenta de [Firebase](https://firebase.google.com/) y seguir los pasos para implementar analytics en el proyecto (google_services.json)
- Generar API-KEYS e incluirlas en el /app/src/main/java/com/example/lexiapp/domain/model/Secrets.kt
    - OPENAI_API_KEY: Crear cuenta en [Open AI](https://platform.openai.com/docs/api-reference) y agregarla en el archivo mencionado con en siguiente formato
    ```kotlin
    class Secrets {
        companion object {
            const val OPENAI_API_KEY = "YOUR-SECRET-KEY"
        }
    }
    ```
- Compilar en AndroidStudio con JDK 11 e instalar en dispositivo o emulador
![image](https://github.com/TheLastColor/lexi-app/assets/82070877/9596def1-03e8-4832-a61c-ac129ff836f4)

## Compilacion paso a paso
- En el menú del dispositivo de destino, selecciona el dispositivo en el que deseas ejecutar la app.
![image](https://github.com/TheLastColor/lexi-app/assets/82070877/24bd8425-9d68-4bba-929d-2d05be30ad1b)
- Si no tienes ningún dispositivo configurado, debes [crear un dispositivo virtual de Android](https://developer.android.com/studio/run/managing-avds?hl=es-419#createavd) para usar [Android Emulator](https://developer.android.com/studio/run/emulator?hl=es-419) o [conectar un dispositivo físico](https://developer.android.com/studio/run/device?hl=es-419#connect).

### Arquitectura
Se implemento una arquitectura MVVM con inyeccion de dependencias con [Dagger-Hilt](https://dagger.dev/hilt/)

<p align="center" width="100%">
    <img src="./docs/Arquitectura.drawio.png"> 
</p>

#### Packages
- configuration 
    * Configuración de la base de datos local
- data
    * Modelo de datos de la capa de datos, implementacion de los servicios que interactuan con los gateways o repositories y gateways o repositories
- di (Dependency Injection)
    * Modulos de Dagger-Hilt para la inyeccion de dependencias
- domain
    - model
        - Modelos de datos de la capa de dominio
    - service
        - Interfaces de servicios
    - useCases
        - Casos de uso de las distintas actividades (Lógica de negocio)
- ui
    - Capa de vistas (Fragments, Activities y ViewModels)


