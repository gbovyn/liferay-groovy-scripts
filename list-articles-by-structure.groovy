import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.journal.service.JournalArticleLocalServiceUtil

final DUTCH_LOCALE = new Locale("nl_BE")

allStructures = DDMStructureLocalServiceUtil.getStructures()

oldStructures = getOldStructures(allStructures)

oldStructures.each {
    structureKey = it.getStructureKey()
    String[] keys = [structureKey]
    articles = JournalArticleLocalServiceUtil.getStructureArticles(keys).toList()

    removeDuplicates(articles)

    out.println(structureKey + " - " + it.getName() + " (size: " + articles.size() + ")")

    articles.each {
        article = JournalArticleLocalServiceUtil.getLatestArticle(it.getResourcePrimKey())
        articleId = article.getArticleId()
        out.println("\t* " + article.getTitle(DUTCH_LOCALE) + " (id: <a href='" + getArticleUrl(articleId) + "'>" + articleId + "</a>) ")
    }
    out.println()
}

def List getOldStructures(List allStructures) {
    allStructures.stream().filter { structure -> structure.getUserName().contains("Webmaster") }.collect()
}

def List removeDuplicates(List articles) {
    Set uniqueArticles = []

    articles.removeIf { !uniqueArticles.add(it.getArticleId()) }.collect { }
}

def String getArticleUrl(String articleId) {
    return "/group/private-pv/~/control_panel/manage?p_p_id=com_liferay_journal_web_portlet_JournalPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_com_liferay_journal_web_portlet_JournalPortlet_mvcPath=%2Fedit_article.jsp&_com_liferay_journal_web_portlet_JournalPortlet_articleId=" + articleId
}
