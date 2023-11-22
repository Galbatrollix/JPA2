package controllers;


import mongodbRepo.MongoRepository;


public abstract class AbstractController {
    protected static MongoRepository repo;


    public static void attachMongoRepository(){
        MongoRepository mongoRepo = new MongoRepository();
        mongoRepo.initDbConnection("bookDB");

        AbstractController.repo = mongoRepo;
    }

    public static  void initMongoCollections(){
        repo.createCollections();
    }

    public static void closeMongoRepository(){
        AbstractController.repo.close();

    }



}

