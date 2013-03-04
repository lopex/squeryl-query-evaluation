package test

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import org.squeryl.{SessionFactory, Session}
import org.squeryl.adapters.H2Adapter


object Test {
    case class Tab(val id: Long, val name: String)

    object S extends Schema {
        val tab = table[Tab]("Tab")
    }


    def main(args: Array[String]) {
        Class.forName("org.h2.Driver")
        val url = "jdbc:h2:mem:dbname;DB_CLOSE_DELAY=-1"

        SessionFactory.concreteFactory = Some(()=> Session.create(java.sql.DriverManager.getConnection("jdbc:h2:mem:dbname;DB_CLOSE_DELAY=-1"), new H2Adapter()))


        transaction {
            S.create
            1 to 10 foreach { e =>
                S.tab.insert(Tab(e, "name" + e))
            }
        }

        transaction {
            val st = from(S.tab)(t => {println("How many times am I being called?");select(t)})
            st.toList
        }

    }
}