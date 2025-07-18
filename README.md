# Unified Notification System

## Proof of Concept

Example calls:
```
curl http://localhost:8001/events
curl -d '{"agencyID":"ca-yolo-da","status":"broken"}' http://localhost:8001/events
curl http://localhost:8001/statuses
```

## Running API Locally

[localtunnel](https://localtunnel.github.io/www/) can be used to expose the API server to an external IP: 
- npm install -g localtunnel
- lt --port 8001

Use the returned URL for the `curl` commands
```
curl https://x-noun-verb.loca.lt/events
curl -d '{"agencyID":"ca-yolo-da","status":"new data"}' http://x-noun-verb.loca.lt/events
```
