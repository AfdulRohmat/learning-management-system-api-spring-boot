# Course Data API Spec

## Create Course Data

Endpoint : POST /api/v1/course/course-data

Request Header :

- Authorization : Bearer token

Request Body :

```json
{
  "title": "Introduction",
  "description": "Video desc fot introduction",
  "video_length": 12,
  "video_url": "https://alamat-video-introduction",
  "course_id": 1,
  "question": [
  ]
}
```

Response Body (Success) :

```json
{
  "success": true,
  "message": "Success",
  "data": null
}
```

Response Body (Failed) :

```json
{
  "success": false,
  "message": "jwt expired",
  "data": null
}
```