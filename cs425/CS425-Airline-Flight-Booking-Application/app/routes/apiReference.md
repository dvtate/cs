# booking_routes:

## `/bookings/add`:


### Input:

```JSON
{
	 "route": [
        [
          578024,
          "7F",
          "07A",
          "04A",
          50,
          "Sun, 29 Dec 2019 19:45:00 GMT",
          "Mon, 30 Dec 2019 12:15:00 GMT",
          240,
          15
        ],
        [
          306388,
          "3U",
          "04A",
          "1N7",
          47,
          "Mon, 30 Dec 2019 13:45:00 GMT",
          "Mon, 30 Dec 2019 21:45:00 GMT",
          120,
          20
        ]
	 ],
	"routeClass": "economy"
}
```

### Output:

```json
{
    "message": "New bookings has been added."
}, 200
```

## `/bookings/get`:

### Input:

Only argument is `customerId` in header.

### Output:
```JSON
{
  "1": {
    "route": [
      [
        32815,
        "FP",
        "7AK",
        "1NY",
        282,
        "Sun, 26 Jan 2020 20:15:00 GMT",
        "Mon, 27 Jan 2020 16:45:00 GMT",
        240,
        10
      ]
    ],
    "routeData": {
      "flightTime": 0.0,
      "routeCost": 271.81
    }
  },
  "2": {
    "route": [
      [
        578024,
        "7F",
        "07A",
        "04A",
        50,
        "Sun, 29 Dec 2019 19:45:00 GMT",
        "Mon, 30 Dec 2019 12:15:00 GMT",
        240,
        15
      ],
      [
        306388,
        "3U",
        "04A",
        "1N7",
        47,
        "Mon, 30 Dec 2019 13:45:00 GMT",
        "Mon, 30 Dec 2019 21:45:00 GMT",
        120,
        20
      ]
    ],
    "routeData": {
      "flightTime": 34200.0,
      "routeCost": 775.0
    }
  }
}
```

## `/bookings/delete`

### Input:
```json
{
  "bookingId": 2
}
```

### Output:
```json
{
  "message": "Booking removed."
}
```
