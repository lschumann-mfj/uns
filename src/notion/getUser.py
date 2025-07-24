#!/usr/bin/env python3

import os
import json
from notion_client import Client

notion = Client(auth=os.environ["NOTION_TOKEN"])

response = notion.users.list()

print(json.dumps(response, indent=4))
