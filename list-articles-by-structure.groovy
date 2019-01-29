import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.journal.model.JournalArticle
import com.liferay.journal.service.JournalArticleLocalServiceUtil

import static com.liferay.portal.kernel.template.TemplateConstants.LANG_TYPE_VM as vm

final DUTCH_LOCALE = new Locale("nl_BE")

allStructures = DDMStructureLocalServiceUtil.getStructures()

oldStructures = getOldStructures(allStructures)

oldStructures.each {
    structureKey = it.getStructureKey()
    String[] keys = [structureKey]
    articles = JournalArticleLocalServiceUtil.getStructureArticles(keys).toList()

    removeDuplicates(articles)

    println("${structureKey} - ${it.getName()} (size: ${articles.size()})")

    articles.each {
        article = JournalArticleLocalServiceUtil.getLatestArticle(it.getResourcePrimKey())
        articleId = article.getArticleId()
        println("\t* ${article.getTitle(DUTCH_LOCALE)} (id: <a href='${getArticleUrl(article)}'>${articleId}</a>)")
    }
    println()
}

List getOldStructures(List allStructures) {
    oldStructures = []

    allStructures.each { structure ->
        List templates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(structure.getGroupId(), structure.getStructureId())

        templates.each { template ->
            if (template.getLanguage().equals(vm) && !oldStructures.contains(structure)) {
                oldStructures.add(structure)
            }
        }
    }
    
    return oldStructures
}

List removeDuplicates(List articles) {
    Set uniqueArticles = []

    articles.removeIf { !uniqueArticles.add(it.getArticleId()) }.collect { }
}

String getArticleUrl(JournalArticle article) {
    return """/group/guest/~/control_panel/manage?p_p_id=com_liferay_journal_web_portlet_JournalPortlet\
&_com_liferay_journal_web_portlet_JournalPortlet_mvcPath=/edit_article.jsp\
&_com_liferay_journal_web_portlet_JournalPortlet_groupId=${article.getGroupId()}\
&_com_liferay_journal_web_portlet_JournalPortlet_folderId=${article.getFolderId()}\
&_com_liferay_journal_web_portlet_JournalPortlet_articleId=${article.getArticleId()}\
&_com_liferay_journal_web_portlet_JournalPortlet_version=${article.getVersion()}"""
}
