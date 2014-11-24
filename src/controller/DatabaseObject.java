package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.faces.bean.*;

import entities.Post;

@ManagedBean
@ApplicationScoped
public class DatabaseObject {
	public List<Post> getPostList() {
		/**
		 * Return Posts that is not deleted and published
		 */
		
		DatabaseUtility dbUtil = DatabaseUtility.getInstance();
		ResultSet rs = dbUtil
				.execute("SELECT * FROM `post` WHERE `is_deleted` = 0 AND `is_published` = 1");

		List<Post> result = new ArrayList<Post>();
		try {
			while (rs.next()) {
				Post post = new Post();
				post.setId(rs.getInt(1));
				post.setTitle(rs.getString(3));
				post.setContent(rs.getString(4));
				post.setDate(rs.getDate(5));
				result.add(post);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error at DatabaseObject.getPostList()");
			System.exit(10);
		}
		
		return result;
	}
}
