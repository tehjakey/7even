package com.seven

import org.springframework.dao.DataIntegrityViolationException

class RatingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [ratingInstanceList: Rating.list(params), ratingInstanceTotal: Rating.count()]
    }

    def create() {
        [ratingInstance: new Rating(params)]
    }

    def save() {
        def ratingInstance = new Rating(params)
        if (!ratingInstance.save(flush: true)) {
            render(view: "create", model: [ratingInstance: ratingInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'rating.label', default: 'Rating'), ratingInstance.id])
        redirect(action: "show", id: ratingInstance.id)
    }

    def show(Long id) {
        def ratingInstance = Rating.get(id)
        if (!ratingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "list")
            return
        }

        [ratingInstance: ratingInstance]
    }

    def edit(Long id) {
        def ratingInstance = Rating.get(id)
        if (!ratingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "list")
            return
        }

        [ratingInstance: ratingInstance]
    }

    def update(Long id, Long version) {
        def ratingInstance = Rating.get(id)
        if (!ratingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (ratingInstance.version > version) {
                ratingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'rating.label', default: 'Rating')] as Object[],
                          "Another user has updated this Rating while you were editing")
                render(view: "edit", model: [ratingInstance: ratingInstance])
                return
            }
        }

        ratingInstance.properties = params

        if (!ratingInstance.save(flush: true)) {
            render(view: "edit", model: [ratingInstance: ratingInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'rating.label', default: 'Rating'), ratingInstance.id])
        redirect(action: "show", id: ratingInstance.id)
    }

    def delete(Long id) {
        def ratingInstance = Rating.get(id)
        if (!ratingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "list")
            return
        }

        try {
            ratingInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'rating.label', default: 'Rating'), id])
            redirect(action: "show", id: id)
        }
    }
}
