![PhraseApp](https://img.shields.io/badge/PhraseApp-blue) ![Android](https://img.shields.io/badge/ANDROID-green)

### PhraseDroid
PhraseApp API plugin to use into <strong>Android Studio</strong>.Useful for multi-language projects with API integration.

- Fetch Key info from API
- List translations using KEY
- Escape translation Key when has HTML data with markup
- Auto escape invalid ' (quote) char on resource strings
- Add or replace Keys on string XML resources

#### How to use
- Configure the API Key and Project ID on the settings page.
- Use the shortcut <strong>Ctrl + Shift + P</strong> to run the action.
- Set the translation key on dialog input to fetch the translations from the API.
- View the translations list before change resource files
- Add or remove markup from content when required (when having html templates)
- Insert/update then into resource files


#### API configuration file example:
```
{
  "id": "required: API project ID",
  "key": "required: API project auth key",
  "agent_email": "optional: organization e-mail",
  "agent_url": "optional: organization contact page"
}
```