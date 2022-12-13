import datetime
import azure.functions as func
import requests
app = func.FunctionApp()

# Learn more at aka.ms/pythonprogrammingmodel

# Get started by running the following code to create a function using a HTTP trigger.

app = func.FunctionApp()
@app.function_name(name="timer")
@app.schedule(schedule="0 */30 * * * *", arg_name="mytimer", run_on_startup=True,
              use_monitor=False) 
def test_function(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()
    r = requests.get("http://mktrends.azurewebsites.net/internal/force-gen")
    print(r.status_code)
