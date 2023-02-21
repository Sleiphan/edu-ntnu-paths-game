import java.util.List;

/**
 * A smaller part of a story. It's possible to go between passages via links
 */
public class Passage {
    private final String title;
    private final String content;
    private  final List<Link> links;
    private int hashCode;
    private boolean recalculateHash;

    /**
     * Creates a new Passage-object. A Passage is a smaller part of a story
     * @param title     A description that works as an identifier for the passage
     * @param content   A representation of paragraph or a part of dialog
     * @param links     Connects the passage together with other passages
     */
    public Passage(String title, String content, List<Link> links){
        this.title = title;
        this.content = content;
        this.links = links;

        recalculateHash = true;
        hashCode = this.hashCode();
    }

    /**
     * Returns this passage objects title
     * @return this passage objects title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns this passage objects content, which is the same as the story that unfolds in the passage
     * @return this passage objects content, which is the same as the story that unfolds in the passage
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns this passage objects list of possible links
     * @return this passage objects list of possible links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Adds a link to this passage-objects list of possible links
     * @param link  Link to add to list of possible links
     * @return true if link was added successfully, false if not
     */
    public boolean addLink(Link link){
        for(Link i : links){
            if(i.equals(link)){
                return false;
            }
        }
        links.add(link);
        hashCode = this.hashCode();
        return true;
    }

    /**
     * Checks if this passage objects list of links contains any links
     * @return true if list contains links, false if not
     */
    public boolean hasLinks(){
        return links.size() >= 1;

    };

    /**
     * Returns a string containing a comprehensive overview of this passage object
     * @return a string containing a comprehensive overview of this passage object
     */
    @Override
    public String toString() {
        return "Passage{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", links=" + links.toString() +
                '}';
    }

    /**
     * Checks if this passage object is equal to the submitted parameter object
     * @param passage The passage-object to compare to
     * @return  true if - and only if - the parameter 'passage' is an instance of this passage-class
     *          and this object's state is equal to that of the parameter. Else, returns false
     */
    @Override
    public boolean equals(Object passage) {
        if(passage instanceof  Passage){
            return hashCode() == ((Passage) passage).hashCode;
        }
        else return false;
    }

    /**
     * Returns this objects hashcode. The hashcode is calculated by multiplying the
     * hashcode of this class's fields together
     * Calculation of the hashcode happens in this method, but only if changes to this object's
     * state has been made.
     * This allows for faster successive access to this attribute.
     * @return the hashcode of this object.
     */
    @Override
    public int hashCode() {
        if (recalculateHash){
            hashCode = title.hashCode() * content.hashCode() * links.hashCode();
            recalculateHash = false;
        }
        return hashCode;
    }


}
