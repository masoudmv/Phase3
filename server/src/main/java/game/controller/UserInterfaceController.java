package game.controller;

import game.model.FinalPanelModel;
import server.GameData;
import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import game.model.charactersModel.*;
import game.model.entities.Ability;


import game.model.entities.Skill;

import server.DataBase;


import java.awt.*;
import java.awt.geom.Point2D;
import java.util.UUID;

import static shared.Model.EntityType.*;

public abstract class UserInterfaceController {


    public static void createEpsilonView(String id, String gameID, Color color){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);

        gameData.createEntity(id, epsilon);
    }

    public static void createSquarantineView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, squarantine);
    }

    public static void createTrigorathView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, trigorath);
    }

    public static void creatBulletView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, bullet);
    }


    public static void createCollectibleView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, collectible);
    }

    public static void createFinalPanelView(String id, Point2D location, Dimension size, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createPanel(id, location, size);
    }

    public static void createBarricadosView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, barricados);
    }




    public static void createOmenoctView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, simplePolygon);
    }


    public static void createBabyEpsilonView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, babyEpsilon);
    }


    public static void createLaserView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, laser);
    }

    public static void createArchmireView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, archmire);
    }

    public static void createNecropickView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, necropick);
    }

    public static void createBabyArchmireView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, babyArchmire);
    }


    public static void createGeoShapeView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, orb);
    }

    public static void createNonrigidBulletView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.createEntity(id, nonrigidBullet);
    }




    public static void eliminateGeoShapeView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.eliminateEntity(id);
        gameData.getCreatedEntities().remove(id);
    }
    public static void removeFinalPanelView(String id, String gameID){
        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.eliminateEntity(id);
        gameData.getCreatedPanels().remove(id);

    }
//
//    public static void createSmileyAOEView(String id){ new SmileyAOE(id); }
//    public static void createGeoShapeView(String id, Image image, int zOrder){
//        new GeoShapeView(id, image, zOrder);
//    }
//
//    public static void createNecropickView(String id, Image image){
//        new NecropickView(id, image);
//    }
//
//
//
//    public static void createGeoShapeView(String id){ new GeoShapeView(id); }
//
//
//
//
//
//
//
//
//    public static void updateNecropick(String id){
//        NecropickModel m = findNecropickModel(id);
//        NecropickView v = findNecropickView(id);
//        assert m != null;
//        assert v != null;
//        v.showNextLocation = m.isHovering();
//    }



    public static Skill findSkill(String name) {
        for (Skill.SkillType type : Skill.SkillType.values()) {
            for (Skill skill : Skill.values()) {
                if (skill.getType().equals(type) && skill.getName().equals(name)) return skill;
            }
        }
        return null;
    }

    public static void fireSkill(String gameID, String macAddress) {
        if (findGame(gameID).getProfile().activeAbilities.get(macAddress) != null) {
            Skill.activeSkill.fire(gameID, macAddress);
        }
    }

    public static void fireAbility(String gamID, String macAddress) {
        if (findGame(gamID).getProfile().activeAbilities.get(macAddress) != null) {
            findGame(gamID).getProfile().activeAbilities.get(macAddress).fire(gamID, macAddress);
            findGame(gamID).getProfile().activeAbilities.put(macAddress, null);
        }
    }

    private static Game findGame(String gameID){
        return DataBase.getDataBase().findGame(gameID);
    }

//    public static boolean isGameRunning() {
//        return GameLoop.getINSTANCE().isRunning();
//    }
//
//    public static boolean isGameOn() {
//        return GameLoop.getINSTANCE().isOn();
//    }



}