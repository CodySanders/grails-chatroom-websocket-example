class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"authentication", action:"index")
        "/chat"(controller:"chat", action:"index")
        "500"(view:'/error')
	}
}
