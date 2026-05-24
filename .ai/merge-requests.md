# Merge Request Guidelines

## Merge Request Title

MR titles MUST follow conventional commit format just like commits, because the MR title will be used as the commit on
the default branch. Don't forget to add the issue number at the end of the title.

**Format:** `<type>(<scope>): <description> #<issue-number>`

## Merge Request Description

If there are related issues, add a reference to the description of the merge request under the heading `References`.  
Add a reference like "Closes #<issue-number>" under `References` when the MR resolve an issue.

ALWAYS use the MR templates available in this project (under `.gitlab/merge_request_templates/`) according to the work
done in this MR.

Add quick commands at the bottom of the description:

- Add a line with `/assign me` so the MR is assigned to the author.
- Add lines with `/label <label>` where the `<label>` corresponds to the related issue labels.
    - You may add additional labels according to the changes made in the MR.