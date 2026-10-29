# Checklist Template — FT-0X

> Copiar por feature a `specs/FT-0X/checklists/`.

- [ ] Spec 7 secciones + Aprobacion resuelta
- [ ] Capas respetadas (`presentation -> domain <- data`), sin I/O en `domain/`
- [ ] `compileDebugKotlin` verde antes de tests
- [ ] Gates `lint/detekt/ktlintCheck/testDebugUnitTest/koverVerify` verdes
- [ ] Sin `TODO/FIXME/STOPSHIP` sin issue
- [ ] Logger sanitizado, sin PII ni tokens
- [ ] `Memoria.md` actualizado con decisiones y evidencia
