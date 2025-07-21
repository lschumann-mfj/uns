#!/usr/bin/env python3

import os
from notion_client import Client
from pprint import pprint

notion = Client(auth=os.environ["NOTION_TOKEN"])

list_users_response = notion.users.list()
pprint(list_users_response)
