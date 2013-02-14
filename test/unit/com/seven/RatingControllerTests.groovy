package com.seven

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(RatingController)
@Mock(Rating)
class RatingControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
		params["label"] = "G"
        params["order"] = "1"
    }

    void testIndex() {
        controller.index()
        assert "/rating/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.ratingInstanceList.size() == 0
        assert model.ratingInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.ratingInstance != null
    }

    void testSave() {
        controller.save()

        assert model.ratingInstance != null
        assert view == '/rating/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/rating/show/1'
        assert controller.flash.message != null
        assert Rating.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/rating/list'

        populateValidParams(params)
        def rating = new Rating(params)

        assert rating.save() != null

        params.id = rating.id

        def model = controller.show()

        assert model.ratingInstance == rating
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/rating/list'

        populateValidParams(params)
        def rating = new Rating(params)

        assert rating.save() != null

        params.id = rating.id

        def model = controller.edit()

        assert model.ratingInstance == rating
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/rating/list'

        response.reset()

        populateValidParams(params)
        def rating = new Rating(params)

        assert rating.save() != null

        // test invalid parameters in update
        params.id = rating.id
        //TODO: add invalid values to params object
		params["order"] = null
		params["label"] = null
		
        controller.update()

        assert view == "/rating/edit"
        assert model.ratingInstance != null

        rating.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/rating/show/$rating.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        rating.clearErrors()

        populateValidParams(params)
        params.id = rating.id
        params.version = -1
        controller.update()

        assert view == "/rating/edit"
        assert model.ratingInstance != null
        assert model.ratingInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/rating/list'

        response.reset()

        populateValidParams(params)
        def rating = new Rating(params)

        assert rating.save() != null
        assert Rating.count() == 1

        params.id = rating.id

        controller.delete()

        assert Rating.count() == 0
        assert Rating.get(rating.id) == null
        assert response.redirectedUrl == '/rating/list'
    }
}
