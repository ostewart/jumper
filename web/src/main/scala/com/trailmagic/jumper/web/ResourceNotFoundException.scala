package com.trailmagic.jumper.web

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException