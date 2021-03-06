package com.epam.task6.dao.impl;

import com.epam.task6.dao.DAOException;
import com.epam.task6.dao.api.ProjectDAO;
import com.epam.task6.dao.pool.ConnectionPool;
import com.epam.task6.domain.project.Project;
import com.epam.task6.resource.ResourceManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 26.04.15.
 */
public class ProjectDAOImpl implements ProjectDAO {
    /** Initializing database activity logger */
    private static Logger logger = Logger.getLogger("db");

    private static int last_number = 0;
    private static int count_on_page = 100;

    /** Logger messages */
    private static final String ERROR_GET_MANAGER_PROJECTS = "logger.db.error.get.manager.projects";
    private static final String INFO_GET_MANAGER_PROJECTS = "logger.db.info.get.manager.projects";
    private static final String ERROR_SAVE_PROJECT = "logger.db.error.save.project";
    private static final String INFO_SAVE_PROJECT = "logger.db.info.save.project";
    private static final String ERROR_GET_PROJECT = "logger.db.error.get.project";
    private static final String INFO_GET_PROJECT = "logger.db.info.get.project";
    private static final String ERROR_GET_PROJECT_BY_ID = "logger.db.error.get.project.by.id";
    private static final String INFO_GET_PROJECT_BY_ID = "logger.db.info.get.project.by.id";
    private static final String ERROR_GET_LAST_PROJECT_ID = "logger.db.error.get.last.project.id";
    private static final String INFO_GET_LAST_PROJECT_ID = "logger.db.info.get.last.project.id";
    private static final String ERROR_CLOSE = "111";

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_SID = "sid";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_DEVID = "devid";
    private static final String ATTRIBUTE_MANAGER = "manager";
    private static final String ATTRIBUTE_STATUS = "status";

    /**
     * Keeps query which return project created by certain manager. <br />
     * Requires to set manager id.
     */
    private static final String SQL_FIND_MANAGER_PROJECTS =
            "SELECT * FROM projects WHERE manager = ?";
    private static final String SQL_FIND_MANAGER_PROJECTS_WITH_STATUS =
            "SELECT name FROM projects WHERE manager = ? AND status = ? AND devid = 0";

    private static final String SQL_FIND_PROJECTS_BY_STATUS_AND_DIVID=
            "SELECT * FROM projects WHERE status = ? AND devid = ?;";
    /**
     * Keeps query which saves new project in database. <br />
     * Requires to set: project name, manager is, specification id.
     */
    private static final String SQL_SAVE_PROJECT =
            "INSERT INTO projects (name, manager, sid, devid, status) VALUES (?, ?, ?, ?, ?)";

    /**
     * Keeps query which return project created for certain specification. <br />
     * Requires to set specification id.
     */
    private static final String SQL_FIND_PROJECT_BY_SPECIFICATION_ID =
            "SELECT * FROM projects WHERE sid = ?";

    /**
     * Keeps query which searches last project created by certain manager. <br />
     * Requires to set manager id.
     */
    private static final String SQL_FIND_LAST_MANAGER_PROJECT_ID =
            "SELECT id FROM projects WHERE manager = ? ORDER BY id DESC LIMIT 1 ";

    /**
     * Keeps query which searches project by specification id.
     */
    private static final String SQL_FIND_PROJECT_BY_PROJECT_ID =
            "SELECT * FROM projects WHERE id = ?";

    private static final String SQL_FIND_ID_BY_NAME =
            "SELECT id FROM projects WHERE name = ?";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE projects SET status = ? WHERE id = ?";

    private static final String SQL_UPDATE_NAME =
            "UPDATE projects SET name = ? WHERE id = ?";

    private static final String SQL_UPDATE_TIME =
            "UPDATE projects SET time = ? WHERE id = ?";

    private static final String SQL_UPDATE_DEV_ID =
            "UPDATE projects SET devid = ? WHERE id = ?";

    private static  final String SQL_DELETE_PROJECT =
            "DELETE FROM projects WHERE id = ?";


    private static final ProjectDAOImpl instance = new ProjectDAOImpl();
    public static ProjectDAOImpl getInstance() { return  instance; }


    public void deleteProject (int id) throws DAOException {
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_DELETE_PROJECT);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + id);
    }

    public void updateProjectByName(int id, String name) throws DAOException {
       // connector = new DBConnector();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_UPDATE_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + id);
    }

    public void updateProjectByTime(int id, String time) throws DAOException {
        //onnector = new DBConnector();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_UPDATE_TIME);
            preparedStatement.setString(1, time);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + id);
    }


    public void updateStatusById(int id, int status) throws DAOException {
       // connector = new DBConnector();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_UPDATE_STATUS);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + id);
    }

    public int returnIdByName (String name) throws DAOException {
      //  connector = new DBConnector();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int id = 0;
        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_ID_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt(ATTRIBUTE_ID);
            }
        }
        catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        return id;
    }
    /**
     * This method returns list of all manager projects.
     *
     * @param uid Manager id
     * @return List of projects
     * @throws DAOException object if execution of query is failed
     */
    public List<Project> getManagerProjects(int uid) throws DAOException {
        ArrayList<Project> list = new ArrayList<Project>();
        Project project = null;
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_MANAGER_PROJECTS);
            preparedStatement.setInt(1, uid);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            while (count < last_number && resultSet.next()) {
                count++;
            }
            for (; count < last_number + count_on_page && resultSet.next(); count++) {
               project = new Project(resultSet.getInt(ATTRIBUTE_ID),resultSet.getString(ATTRIBUTE_NAME), resultSet.getInt(ATTRIBUTE_SID),
                       resultSet.getString(ATTRIBUTE_TIME),resultSet.getInt(ATTRIBUTE_DEVID), resultSet.getInt(ATTRIBUTE_MANAGER),resultSet.getInt(ATTRIBUTE_STATUS));
                list.add(project);
            }
        } catch (SQLException exception) {
            throw new DAOException(ResourceManager.getProperty(ERROR_GET_MANAGER_PROJECTS) + uid, exception);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
               preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_GET_MANAGER_PROJECTS) + uid);
        return list;
    }



    /**
     * This method returns list of all manager projects.
     *
     * @param uid Manager id
     * @return List of projects
     * @throws DAOException object if execution of query is failed
     */
    public List<String> getManagerProjectsWithStarus(int uid, int status) throws DAOException {

        ArrayList<String> list = new ArrayList<String>();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

            try {
                preparedStatement = connector.prepareStatement(SQL_FIND_MANAGER_PROJECTS_WITH_STATUS);
                preparedStatement.setInt(1, uid);
                preparedStatement.setInt(2, status);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    list.add(resultSet.getString(ATTRIBUTE_NAME));
                }
            } catch (SQLException exception) {
                throw new DAOException(ResourceManager.getProperty(ERROR_GET_MANAGER_PROJECTS) + uid, exception);
            } finally {
                ConnectionPool.getInstance().returnConnection(connector);
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(ResourceManager.getProperty(ERROR_CLOSE));
                }
                try{
                    resultSet.close();
                }
                catch (SQLException e) {
                    logger.error(ResourceManager.getProperty(ERROR_CLOSE));
                }
            }
            logger.info(ResourceManager.getProperty(INFO_GET_MANAGER_PROJECTS) + uid);

        return list;
    }

    public void updateDevId(int id, int devId) throws DAOException {
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_UPDATE_DEV_ID);
            preparedStatement.setInt(1, devId);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
         throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + id, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + id);
    }

    public List<Project> getProjectsByStatusAndDivId(int status, int devid) throws DAOException {

        ArrayList<Project> list = new ArrayList<Project>();
        Project project = null;
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_PROJECTS_BY_STATUS_AND_DIVID);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, devid);

            resultSet = preparedStatement.executeQuery();

            int count = 0;
            while (count < last_number && resultSet.next()) {
                count++;
            }
            for (; count < last_number + count_on_page && resultSet.next(); count++) {
                project = new Project(resultSet.getInt(ATTRIBUTE_ID),resultSet.getString(ATTRIBUTE_NAME), resultSet.getInt(ATTRIBUTE_SID),
                        resultSet.getString(ATTRIBUTE_TIME),resultSet.getInt(ATTRIBUTE_DEVID), resultSet.getInt(ATTRIBUTE_MANAGER),
                        resultSet.getInt(ATTRIBUTE_STATUS));
                list.add(project);
            }
        } catch (SQLException exception) {
            throw new DAOException(ResourceManager.getProperty(ERROR_GET_MANAGER_PROJECTS) + devid, exception);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_GET_MANAGER_PROJECTS) + devid);
        return list;
    }
    /**
     * This method saves new project in database.
     *
     * @param name Project name
     * @param mid Manager id
     * @param sid Specification id
     * @return Id of saved project
     * @throws DAOException object if execution of query is failed
     */
    public int saveProject(String name, int mid, int sid, int did, int status) throws DAOException {
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_SAVE_PROJECT);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2, mid);
            preparedStatement.setInt(3, sid);
            preparedStatement.setInt(4, did);
            preparedStatement.setInt(5, status);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_SAVE_PROJECT) + name, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_SAVE_PROJECT) + sid);
        return getLastProjectId(mid);
    }

    /**
     * This method returns project by specification id.
     *
     * @param sid Specification id
     * @return Project object
     * @throws DAOException object if execution of query is failed
     */
    public Project getProject(int sid) throws DAOException {
        Project project = new Project();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_PROJECT_BY_SPECIFICATION_ID);
            preparedStatement.setInt(1, sid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                project.setId(resultSet.getInt(ATTRIBUTE_ID));
                project.setName(resultSet.getString(ATTRIBUTE_NAME));
                project.setManager(resultSet.getInt(ATTRIBUTE_MANAGER));
                project.setSpetification_id(resultSet.getInt(ATTRIBUTE_SID));
            }
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_GET_PROJECT) + sid, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_GET_PROJECT) + sid);
        return project;
    }

    /**
     * This method returns project object by project id.
     *
     * @param pid Project id
     * @return Project object
     * @throws DAOException object if execution of query is failed
     */
    public Project getProjectById(int pid) throws DAOException {
        Project project = new Project();
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_PROJECT_BY_PROJECT_ID);
            preparedStatement.setInt(1, pid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                project.setId(resultSet.getInt(ATTRIBUTE_ID));
                project.setName(resultSet.getString(ATTRIBUTE_NAME));
                project.setManager(resultSet.getInt(ATTRIBUTE_MANAGER));
                project.setSpetification_id(resultSet.getInt(ATTRIBUTE_SID));
                project.setTime(resultSet.getString(ATTRIBUTE_TIME));
            }
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_GET_PROJECT_BY_ID) + pid, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_GET_PROJECT_BY_ID) + pid);
        return project;
    }

    /**
     * This method returns last created project by certain manager.
     *
     * @param mid Manager id
     * @return Last created project id created by manager
     * @throws DAOException object if execution of query is failed
     */
    public int getLastProjectId(int mid) throws DAOException {
        int id = 0;
        Connection connector = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connector.prepareStatement(SQL_FIND_LAST_MANAGER_PROJECT_ID);
            preparedStatement.setInt(1, mid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException(ResourceManager.getProperty(ERROR_GET_LAST_PROJECT_ID) + mid, e);
        } finally {
            ConnectionPool.getInstance().returnConnection(connector);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
            try{
                resultSet.close();
            }
            catch (SQLException e) {
                logger.error(ResourceManager.getProperty(ERROR_CLOSE));
            }
        }
        logger.info(ResourceManager.getProperty(INFO_GET_LAST_PROJECT_ID) + mid);
        return id;
    }

}
