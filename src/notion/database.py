#!/usr/bin/env python3

import os
import json
from notion_client import Client

notion = Client(auth=os.environ["NOTION_TOKEN"])

response = notion.databases.query(
    **{
        "database_id": "1e3cd964261e80778565c16b21d0d475"
    }
)

print(json.dumps(response, indent=4))
