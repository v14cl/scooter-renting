package com.vlad.scooterrental.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ModuleIsolationTest {
  private static final Path MAIN_SOURCES = Path.of("src/main/java/com/vlad/scooterrental");

  @Test
  void analyticsShouldNotImportCoreInternalPackages() throws IOException {
    String analyticsSources = readSources(MAIN_SOURCES.resolve("analytics"));

    assertFalse(analyticsSources.contains("com.vlad.scooterrental.core.domain"));
    assertFalse(analyticsSources.contains("com.vlad.scooterrental.core.infrastructure"));
    assertFalse(analyticsSources.contains("com.vlad.scooterrental.core.application"));
  }

  @Test
  void coreShouldNotImportAnalyticsPackages() throws IOException {
    String coreSources = readSources(MAIN_SOURCES.resolve("core"));

    assertFalse(coreSources.contains("com.vlad.scooterrental.analytics"));
  }

  @Test
  void coreEventsShouldBePublishedAsPublicContract() throws IOException {
    String coreApiSources = readSources(MAIN_SOURCES.resolve("core/api/event"));

    assertTrue(coreApiSources.contains("record UserRegisteredEvent"));
    assertTrue(coreApiSources.contains("record ScooterCreatedEvent"));
    assertTrue(coreApiSources.contains("record RentalCreatedEvent"));
    assertTrue(coreApiSources.contains("implements IntegrationEvent"));
  }

  private String readSources(Path root) throws IOException {
    StringBuilder result = new StringBuilder();
    try (var paths = Files.walk(root)) {
      for (Path path : paths.filter(Files::isRegularFile).filter(this::isJavaFile).toList()) {
        result.append(Files.readString(path)).append('\n');
      }
    }
    return result.toString();
  }

  private boolean isJavaFile(Path path) {
    return path.toString().endsWith(".java");
  }
}
