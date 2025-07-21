#!/usr/bin/env python3

import os
from notion_client import Client
from pprint import pprint

notion = Client(auth=os.environ["NOTION_TOKEN"])

my_page = notion.databases.query(
    **{
        "database_id": "1e3cd964261e80778565c16b21d0d475"
    }
)
pprint(my_page)
