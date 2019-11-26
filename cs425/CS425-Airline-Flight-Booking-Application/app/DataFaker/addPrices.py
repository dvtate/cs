import db_interface
from datetime import datetime
import random

now = datetime.now()

for i in range(1, 1000000):
    ecoPrice = random.uniform(45, 450)
    firstMultiplier = random.uniform(3, 15)
    firstClassPrice = ecoPrice * firstMultiplier
    db_interface.addPrice(i, round(ecoPrice, 2), round(firstClassPrice, 2), now)
