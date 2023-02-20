package com.shoppingcart.admin.category;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;
import com.shoppingcart.admin.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class CategoryTest {
	@Autowired
	CategoryRepository cateRepo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateRootCategory() {
		Category computer = new Category("Computers");
		cateRepo.save(computer);
		
		Category computerComponents = new Category("Computer Components", computer);
		cateRepo.save(computerComponents);
		
		Category laptops = new Category("latops", computer);
		cateRepo.save(laptops);
		
		Category desktops = new Category("desktops", computer);
		cateRepo.save(desktops);
		
		Category laptops1 = new Category("laptop 1",laptops);
		cateRepo.save(laptops1);
		Category laptops2 = new Category("laptop 2",laptops);
		cateRepo.save(laptops2);
		Category laptops3 = new Category("laptop 3",laptops);
		cateRepo.save(laptops3);
		Category laptops21 = new Category("laptop 21",laptops2);
		cateRepo.save(laptops21);
		Category laptops211 = new Category("laptop 211",laptops21);
		cateRepo.save(laptops211);
		
		Category menoryA = new Category("Memory A",computerComponents);
		cateRepo.save(menoryA);
		Category menoryB = new Category("Memory B",computerComponents);
		cateRepo.save(menoryB);
		Category a1 = new Category("a1",menoryA);
		cateRepo.save(a1);
		Category a2 = new Category("a2",a1);
		cateRepo.save(a2);
		Category a3 = new Category("a3",a2);
		cateRepo.save(a3);
		Category a4 = new Category("a4",a3);
		cateRepo.save(a4);
		Category b1 = new Category("b1",menoryB);
		cateRepo.save(b1);
		Category b2 = new Category("b2",b1);
		cateRepo.save(b2);
		

	}
		@Test
		public void testChildrenCategory() {
				
			 Category computer = entityManager.find(Category.class, 1);
			 Set<Category> listComp = computer.getChildren();
			 System.out.println("Computer's children: ");
			 printChildren(listComp,"--");
		

		
			
		}
		
			 @Test
			 public void printChildren(Set<Category> listcategories,String underScore) {
			
				for (Category category : listcategories) {
					System.out.print(underScore + " ");
					System.out.println(category.getName());
					if(!category.getChildren().isEmpty()) {
						Set<Category> listChildren = category.getChildren();
							printChildren(listChildren, underScore + "--");
						}
					}
			
				}
			 
			 }
			
	

