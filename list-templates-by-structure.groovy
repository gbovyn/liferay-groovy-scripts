import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.journal.service.JournalArticleLocalServiceUtil

final DUTCH_LOCALE = new Locale("nl_BE")

allStructures = DDMStructureLocalServiceUtil.getStructures()

allStructures.each { structure ->
    out.println(structure.getStructureKey() + " - " + structure.getName())
    List templates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(structure.getGroupId(), structure.getStructureId())

    templates.each { template ->
        out.println("    - " + template.getName(DUTCH_LOCALE) + " (language: " + template.getLanguage().toUpperCase() + ")")
    }
}
