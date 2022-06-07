package io.rpg.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class PathUtils {

  /**
   * Resolves path to the asset.
   *
   * Path to the asset must be either relative to the configuration directory
   * or absolute.
   *
   * @param pathStr path to the asset
   * @return optional with path to the asset if resolution succeeded, empty optional else
   */
  public static Optional<String> resolvePathToAsset(Path root, String pathStr) {
    if (pathStr == null) {
      return Optional.empty();
    }

    Path assetPath = Path.of(pathStr);

    if (Files.isRegularFile(assetPath)) {
      return Optional.of(assetPath.toString());
    }

    assetPath = root.resolve(pathStr);

    if (Files.isRegularFile(assetPath)) {
      return Optional.of(assetPath.toString());
    } else {
      return Optional.empty();
    }
  }

  public static String resolvePathToJFXFormat(String path) {
    return "file:" + path;
  }
}