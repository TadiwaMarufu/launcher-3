# EmoLauncher

**Android, in your own frequency.**

EmoLauncher is an open-source Android HOME launcher focused on a calm monochrome visual language, fast interaction, contextual environments and deep customization.

## v0.1 — Milestone 1

This first foundation includes:

- Real Android HOME intent
- App discovery through `PackageManager`
- Dynamic app list
- App launching
- App drawer
- Fast local fuzzy-ish search
- Material 3 / Compose UI
- Monochrome foundation
- GitHub Actions build
- Unit and Compose test scaffolding

## Build

The first CI package builds directly on GitHub Actions. The official Gradle Actions setup provisions Gradle 8.9 on the runner, so no local Gradle installation is required.

The CI workflow runs tests, assembles the debug APK and uploads it as a workflow artifact.

The Gradle wrapper files remain in the project for the next repository-hardening pass.

## Architecture

- `model` — stable domain models
- `data` — Android/package discovery
- `ui` — launcher surfaces and state
- future modules/packages — wallpaper, widgets, profiles, search, smart engine and developer tools

## Design direction

EmoLauncher is intentionally not a terminal, hacker dashboard or cyberpunk launcher. Its developer capabilities will live inside the same consumer-grade design system as the rest of the launcher.

## License

Apache License 2.0.
