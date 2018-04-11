# RemoteConfig
I wrote the app using a simple MVP pattern. The view layer is passive and can only display data provided by the presenter. The presenter requests the config from the model layer and ConfigRepository returns either the remote or cached version (or an error). 

Path to json file is set in ConfigRepositoryImpl with constant FULL_JSON_PATH.
