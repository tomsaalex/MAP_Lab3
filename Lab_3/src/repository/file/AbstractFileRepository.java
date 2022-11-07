package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * A class that extends the functionality of InMemoryRepository so that it can work with files.
 * @param <ID> The type used as an ID by the entities stored in this repository.
 * @param <E> The type of entity stored in this repository. Must have Entity as an upper bound.
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Adds the contents of the file stored at the "fileName" location to the repository.
     */
    private void loadData()
    {
        Path path = Paths.get(fileName);

        try{
            List<String> lines = Files.readAllLines(path);
            for (String line: lines)
            {
                if("".equals(line))
                {
                    continue;
                }
                E newEntity = extractEntityFromLine(line);
                super.save(newEntity);
            }
        } catch(IOException ie)
        {
            System.out.println("Failed to read from file: "+ fileName + " !");
            ie.printStackTrace();
        }
    }

    @Override
    public E save(E entity)
    {
        E foundEntity = super.save(entity);
        if(foundEntity == null)
        {
            writeToFile(entity);
        }
        return foundEntity;
    }

    @Override
    public E delete(ID entityID)
    {
        E deletedEntity = super.delete(entityID);

        if(deletedEntity == null)
            return null;

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false)))
        {
            for(E e: findAll())
            {
                bw.newLine();
                bw.write(createEntityAsString(e));
            }
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }

        return deletedEntity;
    }

    private void writeToFile(E entity) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true)))
        {
            bw.newLine();
            bw.write(createEntityAsString(entity));
        }catch(IOException ie)
        {
            ie.printStackTrace();
        }


    }

    /**
     * Defines how a string should be parsed to extract the relevant information for the repository.
     * @param line The string to be parsed.
     * @return An entity made from the relevant information extracted.
     */
    public abstract E extractEntityFromLine(String line);

    /**
     * Creates a string based on an entity in a way that allows for the entity to be reconstructed from it later.
     * @param entity The entity to transform into a string
     * @return The resulting string.
     */
    public abstract String createEntityAsString(E entity);




}
