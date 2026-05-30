package gitrepositoryinfo

import gitrepositoryinfo.test.model.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GitRepositoryInfoTest {
  @Test
  fun `gitHost is written into generated git push script`() {
    val content = gitPushSh().readText()
    assertTrue(
      content.contains("git_host=\"gitlab.com\""),
      "Expected gitHost in git_push.sh, got:\n$content",
    )
  }

  @Test
  fun `gitUserId is written into generated git push script`() {
    val content = gitPushSh().readText()
    assertTrue(
      content.contains("git_user_id=\"myorg\""),
      "Expected gitUserId in git_push.sh, got:\n$content",
    )
  }

  @Test
  fun `gitRepoId is written into generated git push script`() {
    val content = gitPushSh().readText()
    assertTrue(
      content.contains("git_repo_id=\"my-api-client\""),
      "Expected gitRepoId in git_push.sh, got:\n$content",
    )
  }

  @Test
  fun `releaseNote is written into generated git push script`() {
    val content = gitPushSh().readText()
    assertTrue(
      content.contains("release_note=\"Initial release\""),
      "Expected releaseNote in git_push.sh, got:\n$content",
    )
  }

  @Test
  fun `generated model compiles and has correct enum values`() {
    assertEquals("active", PostDto.StatusEnum.ACTIVE.getValue())
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_git-repository-info_generateOpenAPI@openapi-generator")
  }

  private fun gitPushSh() = outputDir().resolve("git_push.sh")
}