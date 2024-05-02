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

<p align="center" width="100%">
    <img src="https://github.com/thiago-you/phrase-droid/assets/23344256/04107dcb-9e72-4a72-97aa-57f2d58f1746" width="350"/>
    <img src="https://github.com/thiago-you/phrase-droid/assets/23344256/7af01437-cea5-4729-833f-75d03b4ba73e" width="350"/>
    <img src="https://github.com/thiago-you/phrase-droid/assets/23344256/87832a04-0d65-4e77-8282-5e63cefbed91" width="350"/>
    <img src="https://github.com/thiago-you/phrase-droid/assets/23344256/7b3fe4c3-3c83-48f5-a676-c89aba72467a" width="350"/>
</p>